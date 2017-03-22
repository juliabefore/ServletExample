package com.miskevich.db;

import com.miskevich.beans.User;

import javax.sql.PooledConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLHelper {

    public List<User> getAllUsers(PooledConnection pooledConnection){
        List<User> users = new ArrayList<>();

        try {
            Connection connection = pooledConnection.getConnection();

            String query = "select * from users";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                User user = new User();
                user.setId(resultSet.getInt(1));
                user.setLastName(resultSet.getString(2));
                user.setFirstName(resultSet.getString(3));
                user.setSalary(resultSet.getInt(4));
                user.setDateOfBirth(resultSet.getDate(5));
                users.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}
