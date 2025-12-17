package hei.school;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public Connection getDBConnection()  throws SQLException {
        return DriverManager.getConnection(
                System.getenv("jdbc:postgresql://localhost:5432/mini_dish_db"),
                System.getenv("mini_dish_db_manager"),
                System.getenv("123456")
        );
    }
}
