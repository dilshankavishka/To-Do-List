package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;

    private final Connection connection;

    private DBConnection() throws SQLException {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ToDoList","root","12345");
    }
    public static DBConnection getInstance(){
        try {
            return null==instance?instance=new DBConnection():instance;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Connection getConnection(){
        return connection;
    }
}
