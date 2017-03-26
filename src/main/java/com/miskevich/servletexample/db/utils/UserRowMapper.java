package com.miskevich.servletexample.db.utils;

import com.miskevich.servletexample.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    public User map(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setFirstName(resultSet.getString("firstName").trim());
        user.setLastName(resultSet.getString("lastName").trim());
        user.setSalary(resultSet.getLong("salary"));
        user.setDateOfBirth(resultSet.getDate("dateOfBirth"));
        return user;
    }
}
