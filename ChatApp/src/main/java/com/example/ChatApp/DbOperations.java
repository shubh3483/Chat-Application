package com.example.ChatApp;

import java.sql.*;

public class DbOperations {

    private static Connection connection;

    private static Connection getConnection() throws SQLException {
        if(connection==null){
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shubhproj","root","abc@123");
        }
        return connection;
    }
    public static void addUser(String user) throws SQLException {
        getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("insert into user values(null,?,?)");
        preparedStatement.setString(1,user);
        preparedStatement.setDate(2,new Date(System.currentTimeMillis()));
        int rows_affected = preparedStatement.executeUpdate();
        if(rows_affected>0){
            System.out.println("successfully entered into db");
        }else{
            System.out.println("not able to enter into db");
        }
    }

    public static void sendToDb(String user,String message) throws SQLException {
        getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("insert into chatbackup values(?,?)");
        preparedStatement.setString(1,user);
        preparedStatement.setString(2,message);
        int rows_affected = preparedStatement.executeUpdate();
        if(rows_affected>0){
            System.out.println("successfully added in backup");
        }else{
            System.out.println("problem in chat backup");
        }
    }
}
