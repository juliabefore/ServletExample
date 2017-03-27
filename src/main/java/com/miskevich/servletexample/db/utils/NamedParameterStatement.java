package com.miskevich.servletexample.db.utils;

import com.miskevich.servletexample.db.core.DBHelper;
import com.miskevich.servletexample.db.core.QueryGenerator;
import com.miskevich.servletexample.entity.User;
import com.miskevich.servletexample.service.UserService;

import javax.sql.PooledConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamedParameterStatement{

    private List<String> paramFromQuery;

    public NamedParameterStatement(){
        this.paramFromQuery = new ArrayList<>();
    }

    public <T>List<T> query(String query, Map<String, Object> param, RowMapper<T> mapper, Connection connection){
        try{
            PreparedStatement preparedStatement = generatePreparedStatement(query, connection);
            setParametersIntoPreparedStatement(param, preparedStatement);
            preparedStatement.executeUpdate();
            //3) пробежаться по резалт сету и для каждой строки вызвать метод map
            //4) вернуть лист с записями
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return null;
    }

    private void setParametersIntoPreparedStatement(Map<String, Object> param, PreparedStatement preparedStatement) {
        try{
            for (int i = 1; i <= paramFromQuery.size(); i++) {
                Object value = param.get(paramFromQuery.get(i-1));

                if(value.getClass().equals(String.class)){
                    preparedStatement.setString(i, (String) value);
                }else if(value.getClass().equals(Integer.class)){
                    preparedStatement.setInt(i, (Integer) value);
                }else if(value.getClass().equals(Long.class)){
                    preparedStatement.setLong(i, (Long) value);
                }else if(value.getClass().equals(Date.class)){
                    preparedStatement.setDate(i, (Date) value, Calendar.getInstance());
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }


    }

    private PreparedStatement generatePreparedStatement(String query, Connection connection) {

        Pattern pattern = Pattern.compile("(:\\w+)");
        Matcher matcher = pattern.matcher(query);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find())
        {
            String param = matcher.group(1);
            System.out.println(param);
            paramFromQuery.add(param);
            matcher.appendReplacement(stringBuffer, "?");
        }

        String preparedStatementQuery = matcher.appendTail(stringBuffer).toString();
        System.out.println(preparedStatementQuery);
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(preparedStatementQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return preparedStatement;
    }

    public static void main(String[] args) throws SQLException {

        User user = new User();
        user.setId(1);
        user.setFirstName("fn1");
        user.setLastName("ln1");
        user.setSalary(100L);
        user.setDateOfBirth(QueryGenerator.convertDate("04/05/2017"));
        DBHelper dbHelper = new DBHelper();
        String connectionPath = dbHelper.connectionMap.get("SERVLET");
        PooledConnection pooledConnection = dbHelper.getConnectionPool(connectionPath);
        Connection connection = pooledConnection.getConnection();

        String query = "select id, salary from users where id = :id and firstName = :firstName and id = :id";
        String queryInsert = "insert into users(firstName, lastName, salary, dateOfBirth) values(:firstName, :lastName, :salary, :dateOfBirth)";
        String queryUpdate = "update users set firstName = :firstName, lastName = :lastName, salary = :salary, dateOfBirth = :dateOfBirth where id = :id";
        NamedParameterStatement ps = new NamedParameterStatement();
        PreparedStatement preparedStatement = ps.generatePreparedStatement(queryUpdate, connection);
        Map<String, Object> paramsForQuery = UserService.generateParamsForQuery(user);
        System.out.println(paramsForQuery);
        ps.setParametersIntoPreparedStatement(paramsForQuery, preparedStatement);
        preparedStatement.executeUpdate();
        //System.out.println(paramFromQuery);
    }

}
