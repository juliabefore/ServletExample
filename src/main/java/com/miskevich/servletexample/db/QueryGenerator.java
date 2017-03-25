package com.miskevich.servletexample.db;

import com.miskevich.servletexample.enums.SQLMethod;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public abstract class QueryGenerator {

    public static Date convertDate(String dateOfBirth){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date date;
        try {
            date = df.parse(dateOfBirth);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return new Date(date.getTime());
    }

    public static String createSQLInsert(Map<String, String[]> parameterMap){
        StringBuilder query = new StringBuilder();
        StringBuilder columnNames = new StringBuilder();
        StringBuilder aliasName = new StringBuilder();
        columnNames.append(" (");
        aliasName.append(" (");

        inlineQueryVariables(parameterMap, columnNames, aliasName, SQLMethod.INSERT);

        columnNames.append(")");
        aliasName.append(")");

        query.append("INSERT INTO users")
                .append(columnNames)
                .append(" VALUES")
                .append(aliasName);



        return query.toString();
    }

    public static String createSQLUpdate(Map<String, String[]> parameterMap){
        StringBuilder query = new StringBuilder();
        StringBuilder columnNames = new StringBuilder();
        StringBuilder aliasName = new StringBuilder();
        inlineQueryVariables(parameterMap, columnNames, aliasName, SQLMethod.UPDATE);

        query.append("UPDATE users SET ")
                .append(columnNames)
        .append(" WHERE id = :id");

        return query.toString();
    }

    private static void inlineQueryVariables(Map<String, String[]> parameterMap, StringBuilder columnNames, StringBuilder aliasName, SQLMethod method){
        int i = 0;
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()){
            String key = entry.getKey();
            if(i < parameterMap.size() - 1){
                if(method.equals(SQLMethod.INSERT)){
                    columnNames.append(key)
                            .append(", ");

                    aliasName.append(":")
                            .append(key)
                            .append(", ");
                }else if(method.equals(SQLMethod.UPDATE)){
                    columnNames.append(key)
                            .append(" = :")
                            .append(key)
                            .append(", ");
                }
            }else {
                if(method.equals(SQLMethod.INSERT)){
                    columnNames.append(key);

                    aliasName.append(":")
                            .append(key);
                }else if(method.equals(SQLMethod.UPDATE)){
                    columnNames.append(key)
                            .append(" = :")
                            .append(key);
                }
            }
            i++;
        }
    }
}
