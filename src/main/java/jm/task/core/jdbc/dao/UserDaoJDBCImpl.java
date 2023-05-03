package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl extends Util implements UserDao {
    private  final Connection connection = getConnection();

    public UserDaoJDBCImpl() {

    }

    @Override
    public void createUsersTable() { //*
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY," +
                "name VARCHAR(45)," +
                "lastName VARCHAR(45)," +
                "age TINYINT(3) CHECK(age>=0 AND age <= 127))";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            connection.setAutoCommit(false); //***&*
        } catch (SQLException ex){
            try {
                connection.rollback();
            } catch (SQLException e){
                e.printStackTrace();
            }
            ex.printStackTrace();
        }
    }

    public void dropUsersTable() {
         String str = "DROP TABLE IF EXISTS users";
                    //"DROP TABLE IF EXISTS users"
        try (PreparedStatement preparedStatement = connection.prepareStatement(str)) {
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException ex){
            try {
                connection.rollback();
            } catch (SQLException e){
                e.printStackTrace();
            }
            ex.printStackTrace();
        }
    }
    //Исходник
    /*
    public void saveUser(String name, String lastName, byte age) {

        String sql = "INSERT INTO pre-project.users (name, lastName, age) VALUES(?,?,?)";
                     //"insert into users (name, lastName, age) values (?,?,?)")

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            ex.printStackTrace();
        }
    }

     */
    public void saveUser(String name, String lastName, byte age){
        String sql = "insert into users (name, lastName, age) values (?,?,?)";
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(sql)) {
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,lastName);
            preparedStatement.setByte(3,age);
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("SQL Exeption: " + e.getMessage());
        }
    }

    public void removeUserById(long id) { // ?*
        String str = "DELETE FROM users WHERE id = ?" ;
                   //DELETE FROM users WHERE id = ?"
        // запрос
        try (PreparedStatement preparedStatement = connection.prepareStatement(str)) {
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        //return null;
        List<User> list = new ArrayList<>(); //*?
        //Statement statement = null;
        String sql = "SELECT id, name, lastName, age FROM users";
        try (ResultSet resultSet = connection.createStatement().executeQuery(sql)) {
            //
            connection.setAutoCommit(true); //***&*
            while (resultSet.next()) {
                //
                User user = new User();

                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                System.out.println(user);
                list.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE users";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
