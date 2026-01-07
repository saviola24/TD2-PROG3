package hei.school;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    private final DBConnection dbConnection;

    public DataRetriever(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<Ingredient> findIngredients(int page, int size) {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM Ingredient LIMIT ? OFFSET ?";
        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, (page - 1) * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ingredients.add(new Ingredient(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            CategoryEnum.valueOf(rs.getString("category")),
                            rs.getInt("id_dish")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredients;
    }

    public List<Ingredient> createIngredients(List<Ingredient> newIngredients) {
        String checkSql = "SELECT COUNT(*) FROM Ingredient WHERE name = ?";
        String insertSql = "INSERT INTO Ingredient (name, price, category) VALUES (?, ?, ?)";

        try (Connection conn = dbConnection.getDBConnection()) {
            conn.setAutoCommit(false); // [cite: 108]
            try (PreparedStatement checkPstmt = conn.prepareStatement(checkSql);
                 PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {

                for (Ingredient ing : newIngredients) {
                    checkPstmt.setString(1, ing.getName());
                    try (ResultSet rs = checkPstmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            conn.rollback(); // Annule tout [cite: 105]
                            throw new RuntimeException("L'ingrédient " + ing.getName() + " existe déjà");
                        }
                    }
                    insertPstmt.setString(1, ing.getName());
                    insertPstmt.setDouble(2, ing.getPrice());
                    insertPstmt.setString(3, ing.getCategory().name());
                    insertPstmt.executeUpdate();
                }
                conn.commit();
                return newIngredients;
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dish saveDish(Dish dishToSave) {
        String upsert = "INSERT INTO Dish (id, name, dish_type) VALUES (?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, dish_type = EXCLUDED.dish_type";
        try (Connection conn = dbConnection.getDBConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(upsert)) {
                pstmt.setInt(1, dishToSave.getId());
                pstmt.setString(2, dishToSave.getName());
                pstmt.setString(3, dishToSave.getDishType().name());
                pstmt.executeUpdate();

                String unlink = "UPDATE Ingredient SET id_dish = NULL WHERE id_dish = ?";
                try (PreparedStatement psUnlink = conn.prepareStatement(unlink)) {
                    psUnlink.setInt(1, dishToSave.getId());
                    psUnlink.executeUpdate();
                }

                String link = "UPDATE Ingredient SET id_dish = ? WHERE name = ?";
                try (PreparedStatement psLink = conn.prepareStatement(link)) {
                    for (Ingredient ing : dishToSave.getIngredients()) {
                        psLink.setInt(1, dishToSave.getId());
                        psLink.setString(2, ing.getName());
                        psLink.executeUpdate();
                    }
                }
                conn.commit();
                return dishToSave;
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dish findDishById(Integer id) {
        String sql = "SELECT * FROM Dish WHERE id = ?";
        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Dish dish = new Dish(rs.getInt("id"), rs.getString("name"),
                            DishTypeEnum.valueOf(rs.getString("dish_type")));
                    dish.setPrice(rs.getDouble("price"));
                    return dish;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        throw new RuntimeException("Plat introuvable : " + id); // Requis pour test 7-b
    }

    public List<Dish> findDishsByIngredientName(String ingredientName) {
        List<Dish> dishes = new ArrayList<>();
        String sql = "SELECT DISTINCT d.* FROM Dish d " +
                "JOIN Ingredient i ON d.id = i.id_dish " +
                "WHERE i.name ILIKE ?";
        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + ingredientName + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    dishes.add(new Dish(
                            rs.getInt("id"),
                            rs.getString("name"),
                            DishTypeEnum.valueOf(rs.getString("dish_type"))
                    ));
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return dishes;
    }

    public List<Ingredient> findIngredientsByCriteria(String name, CategoryEnum cat, String dish, int page, int size) {
        List<Ingredient> results = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT i.* FROM Ingredient i LEFT JOIN Dish d ON i.id_dish = d.id WHERE 1=1");

        if (name != null) sql.append(" AND i.name ILIKE ?"); // [cite: 121]
        if (cat != null) sql.append(" AND i.category = ?::category_enum");
        if (dish != null) sql.append(" AND d.name ILIKE ?");
        sql.append(" LIMIT ? OFFSET ?");

        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (name != null) pstmt.setString(idx++, "%" + name + "%");
            if (cat != null) pstmt.setString(idx++, cat.name());
            if (dish != null) pstmt.setString(idx++, "%" + dish + "%");
            pstmt.setInt(idx++, size);
            pstmt.setInt(idx, (page - 1) * size);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new Ingredient(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            CategoryEnum.valueOf(rs.getString("category")),
                            rs.getInt("id_dish") // Ajoutez ce 5ème argument [cite: 10]
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }
}