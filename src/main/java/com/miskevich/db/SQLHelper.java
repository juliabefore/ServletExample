package com.miskevich.db;

import com.miskevich.beans.User;
import com.miskevich.enums.SQLMethod;
import org.sql2o.tools.NamedParameterStatement;
import javax.sql.PooledConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class SQLHelper {

    public static List<User> getAllUsers(PooledConnection pooledConnection){
        List<User> users = new ArrayList<>();
        String query = "select * from users";

        try(Connection connection = pooledConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery()){

            while (resultSet.next()){
                User user = new User();
                user.setId(resultSet.getInt(1));
                user.setLastName(resultSet.getString(2));
                user.setFirstName(resultSet.getString(3));
                user.setSalary(resultSet.getLong(4));
                user.setDateOfBirth(resultSet.getDate(5));
                users.add(user);
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    public static User getUserById(PooledConnection pooledConnection, int id){
        User user = new User();
        String query = "SELECT id, firstName, lastName, salary, dateOfBirth FROM users WHERE id = :id";
        try(Connection connection = pooledConnection.getConnection()){
            NamedParameterStatement namedParameterStatement = new NamedParameterStatement(connection, query, false);
            namedParameterStatement.setInt("id", id);
            ResultSet resultSet = namedParameterStatement.executeQuery();

            while (resultSet.next()){
                user.setId(resultSet.getInt(1));
                user.setFirstName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setSalary(resultSet.getLong(4));
                user.setDateOfBirth(resultSet.getDate(5));
            }

            resultSet.close();
            namedParameterStatement.close();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    public static void changeUser(PooledConnection pooledConnection, String query, User user, SQLMethod method){
        try(Connection connection = pooledConnection.getConnection()){
            NamedParameterStatement namedParameterStatement = new NamedParameterStatement(connection, query, false);
            namedParameterStatement.setString("firstName", user.getFirstName());
            namedParameterStatement.setString("lastName", user.getLastName());
            namedParameterStatement.setDate("dateOfBirth", user.getDateOfBirth());

            if(null == user.getSalary()){
                namedParameterStatement.setNull("salary", Types.BIGINT);
            }else {
                namedParameterStatement.setLong("salary", user.getSalary());
            }
            if(method.equals(SQLMethod.UPDATE)){
                namedParameterStatement.setInt("id", user.getId());
            }
            namedParameterStatement.execute();
            namedParameterStatement.close();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
