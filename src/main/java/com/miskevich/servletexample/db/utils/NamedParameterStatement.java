package com.miskevich.servletexample.db.utils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamedParameterStatement {

    public <T>List<T> query(String query, Map<String, Object> param, RowMapper<T> mapper){
        generatePreparedStatement(query);
        return null;
    }

    private String generatePreparedStatement(String query) {

        Pattern pattern = Pattern.compile("(:\\w+)");
        Matcher matcher = pattern.matcher(query);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find())
        {
            matcher.appendReplacement(stringBuffer, "?");
        }

        return matcher.appendTail(stringBuffer).toString();
    }

    public static void main(String[] args) {
        String query = "select id, salary from users where id = :id and firstName = :firstName and id = :id";
        String queryInsert = "insert into users(firstName, lastName, salary, dateOfBirth) values(:firstName, :lastName, :salary, :dateOfBirth)";
        String queryUpdate = "update users set firstName = :firstName, lastName = :lastName, salary = :salary, dateOfBirth = :dateOfBirth";
        NamedParameterStatement ps = new NamedParameterStatement();
        System.out.println(ps.generatePreparedStatement(queryUpdate));
    }
}
