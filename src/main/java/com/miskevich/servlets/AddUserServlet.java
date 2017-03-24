package com.miskevich.servlets;

import com.miskevich.beans.User;
import com.miskevich.db.QueryGenerator;
import com.miskevich.db.SQLHelper;
import com.miskevich.enums.HttpMethod;
import com.miskevich.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.miskevich.app.MyApp.pooledConnectionServlet;

public class AddUserServlet extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {

        Map<String, Object> pageVariables = createPageVariablesMap(request, HttpMethod.GET);
        pageVariables.put("message", "");

        response.getWriter().println(PageGenerator.instance().getPage("add_user.html", pageVariables));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = createPageVariablesMap(request, HttpMethod.POST);
        System.out.println(pageVariables);
        String message = request.getParameter("message");

        response.setContentType("text/html;charset=utf-8");

        if (message == null || message.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        response.sendRedirect("/all_users");
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request, HttpMethod method) {
        Map<String, Object> pageVariables = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        if(method.equals(HttpMethod.POST)){
            String query = QueryGenerator.createSQLInsert(parameterMap);
            User user = populateUser(parameterMap);
            SQLHelper.addUser(pooledConnectionServlet, query, user);
        }

        pageVariables.put("parameters", parameterMap.toString());
        return pageVariables;
    }

    private User populateUser(Map<String, String[]> parameterMap){
          User user = new User();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()){
            String array = Arrays.toString(entry.getValue());
            String value = "";
            if(array.length() > 2){
                value = array.substring(1, array.length() - 1);
            }

            if(entry.getKey().equals("firstName")){
                user.setFirstName(value);
            }else if(entry.getKey().equals("lastName")){
                user.setLastName(value);
            }else if(entry.getKey().equals("dateOfBirth")){
                user.setDateOfBirth(QueryGenerator.convertDate(value));
            }else if(entry.getKey().equals("salary")){
                if(!value.isEmpty()){
                    user.setSalary(Long.valueOf(value));
                }
            }
        }
          return user;
    }


}
