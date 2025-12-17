package hei.school;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    private final DBConnection dbConnection;

    public DataRetriever(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    //6-a
    public List<Ingredient> findIngredients(int page, int size) {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM ingredient LIMIT ? OFFSET ?";

        try (Connection conn = dbConnection.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, size);
            pstmt.setInt(2, (page - 1) * size);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Transformation d'une ligne SQL en objet Java
                    Ingredient ing = new Ingredient(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            CategoryEnum.valueOf(rs.getString("category")),
                            rs.getInt("id_dish")
                    );
                    ingredients.add(ing);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération", e);
        }
        return ingredients;
    }
}
