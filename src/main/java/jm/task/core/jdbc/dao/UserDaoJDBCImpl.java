package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl extends Util implements UserDao {
    //private  final Connection connection = getConnection();

    public UserDaoJDBCImpl() {

    }

    @Override
    public void createUsersTable() { //*
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY," +
                "name VARCHAR(45)," +
                "lastName VARCHAR(45)," +
                "age TINYINT(3) CHECK(age>=0 AND age <= 127))";
        try (PreparedStatement preparedStatement = Util.connectionJDBC.prepareStatement(sql)){
            Util.connectionJDBC.setAutoCommit(false);
            preparedStatement.executeUpdate();
            Util.connectionJDBC.commit();
        } catch (SQLException ex){
            try {
                connectionJDBC.rollback();
                //Util.connectionJDBC.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
            ex.printStackTrace();
        }
        finally {
            try {
                Util.connectionJDBC.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Error setAutoCommit");
            }
        }
    }

    public void dropUsersTable() {
         String str = "DROP TABLE IF EXISTS users";

         try (PreparedStatement preparedStatement = Util.connectionJDBC.prepareStatement(str)) {
             Util.connectionJDBC.setAutoCommit(false);
             preparedStatement.executeUpdate();
             Util.connectionJDBC.commit();
         } catch (SQLException e) {
             try {
                 connectionJDBC.rollback();
                 //connectionJDBC.close();
             } catch (SQLException ex) {
                 throw new RuntimeException(ex);
             }
             throw new RuntimeException("Error with delete a table");
         }
         finally {
             try {
                 Util.connectionJDBC.setAutoCommit(true);
             } catch (SQLException e) {
                 throw new RuntimeException("Error with AutoCommit");
             }
         }
    }

    public void saveUser(String name, String lastName, byte age){
        String sql = "insert into users (name, lastName, age) values (?,?,?)";

        try (PreparedStatement preparedStatement = Util.connectionJDBC
                .prepareStatement(sql)) {
            Util.connectionJDBC.setAutoCommit(false);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,lastName);
            preparedStatement.setByte(3,age);
            preparedStatement.execute();
            Util.connectionJDBC.commit();
        } catch (SQLException e) {
            try {
                connectionJDBC.rollback();
                //connectionJDBC.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException("Error with records to table");
        }
        finally {
            try {
                Util.connectionJDBC.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Error with AutoCommit");
            }
        }
    }

    public void removeUserById(long id) { // ?*
        String str = "DELETE FROM users WHERE id = ?" ;

        try (PreparedStatement preparedStatement = Util.connectionJDBC.prepareStatement(str)) {
            Util.connectionJDBC.setAutoCommit(false);
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
            Util.connectionJDBC.commit();
        } catch (SQLException e) {
            try {
                connectionJDBC.rollback();
                //connectionJDBC.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException("Error with deleted by ID");
        }
        finally {
            try {
                connectionJDBC.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Error with auto commit");
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>(); //*?
        //Statement statement = null;
        String sql = "SELECT id, name, lastName, age FROM users";

        try (PreparedStatement preparedStatement = Util.connectionJDBC.prepareStatement(sql)) {
            Util.connectionJDBC.setAutoCommit(false);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                User user = new User(resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getByte(4)); // создали Юзера и передали аргументы
                user.setId(resultSet.getLong(1)); //записываем Id
                list.add(user);
            }
            Util.connectionJDBC.setAutoCommit(false);
        } catch (SQLException e) {
            /*
            try {
                Util.connectionJDBC.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
             */
            throw new RuntimeException("Error with select from table");
        }
        return list;
    }

    @Override
    public void cleanUsersTable() {
        String sql = "DELETE FROM " + Util.dbName + "." + Util.dbTableName;

        try (PreparedStatement preparedStatement = Util.connectionJDBC.prepareStatement(sql);) {
            Util.connectionJDBC.setAutoCommit(false);
            preparedStatement.execute();
            Util.connectionJDBC.commit();

        } catch (SQLException e) {
            try {
                Util.connectionJDBC.rollback();
                //Util.connectionJDBC.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException("Error with cleaning table");
        }
        finally {
            try {
                Util.connectionJDBC.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException("Error setAutoCommit(true)");
            }
        }
    }
}
