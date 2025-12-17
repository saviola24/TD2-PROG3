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
        String sql = "SELECT * FROM ingredients LIMIT ? OFFSET ?";

        try(Connection conn = dbConnection.getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setInt(1,size);
            pstmt.setInt(2, (page - 1) * size);

            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){

                }
            }
        }catch (SQLException e){
            throw new RuntimeException("Erreur lors de la recuperation", e);
        }
        return ingredients;
    }
}
