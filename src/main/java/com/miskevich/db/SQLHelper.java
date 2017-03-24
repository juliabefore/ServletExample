package com.miskevich.db;

import com.miskevich.beans.User;
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

    public static void addUser(PooledConnection pooledConnection, String query, User user){

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


            namedParameterStatement.execute();

        }catch (SQLException e){
                throw new RuntimeException(e);
        }


    }
}
