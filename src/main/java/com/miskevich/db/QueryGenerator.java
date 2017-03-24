package com.miskevich.db;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public abstract class QueryGenerator {

    public static Date convertDate(String dateOfBirth){
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
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
        int i = 0;
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()){
            String key = entry.getKey();
            if(i < parameterMap.size() - 1){

                columnNames.append(key)
                        .append(", ");

                aliasName.append(":")
                        .append(key)
                        .append(", ");
            }else {
                columnNames.append(key);

                aliasName.append(":")
                        .append(key);

            }
            i++;
        }

        columnNames.append(")");
        aliasName.append(")");

        query.append("INSERT INTO users")
                .append(columnNames)
                .append(" VALUES")
                .append(aliasName);



        return query.toString();
    }
}
