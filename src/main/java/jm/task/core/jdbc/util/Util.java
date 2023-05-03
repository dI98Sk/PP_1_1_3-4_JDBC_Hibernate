package jm.task.core.jdbc.util;

import java.rmi.ConnectIOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydbtest";
    private static final String DB_USERNAME = "root";
    private static final  String DB_PASSWORD = "Fors0897danna";

    public static Connection getConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
            // ручное управление транзакцией
             connection.setAutoCommit(false);

        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return connection;
    }
}
