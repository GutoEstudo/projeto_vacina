package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    String url = "jdbc:mysql://localhost:3306/projeto_vacina";
    String username = "admin";
    String password = "Arthur_653123!";
    Connection connection = null;
    
    public Connection createConnection() throws SQLException {
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database!");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database!");
            e.printStackTrace();
        }

        return connection;

    }


}
