package com.miskevich.servletexample.servlets;

import com.miskevich.servletexample.entity.User;
import com.miskevich.servletexample.db.QueryGenerator;
import com.miskevich.servletexample.db.SQLHelper;
import com.miskevich.servletexample.enums.HttpMethod;
import com.miskevich.servletexample.enums.SQLMethod;
import com.miskevich.servletexample.service.UserService;
import com.miskevich.servletexample.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.miskevich.servletexample.app.MyApp.pooledConnectionServlet;

public class EditUserServlet extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {

        Map<String, Object> pageVariables = createPageVariablesMap(request, HttpMethod.GET);
        pageVariables.put("message", "");

        response.getWriter().println(PageGenerator.instance().getPage("edit_user.html", pageVariables));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        createPageVariablesMap(request, HttpMethod.POST);
        ServletHelper.defaultPost(request, response);
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request, HttpMethod method) {
        Map<String, Object> pageVariables = new HashMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        pageVariables.put("pathInfo", request.getPathInfo());
        User user = SQLHelper.getUserById(pooledConnectionServlet, getUserId(pageVariables));
        pageVariables.put("user", user);
        if(method.equals(HttpMethod.POST)){
            String query = QueryGenerator.createSQLUpdate(parameterMap);
            Map<String, String[]> editedParameterMap = request.getParameterMap();
            User editedUser = UserService.populateUser(editedParameterMap);
            editedUser.setId(user.getId());
            SQLHelper.changeUser(pooledConnectionServlet, query, editedUser, SQLMethod.UPDATE);
        }
        return pageVariables;
    }

    private int getUserId(Map<String, Object> pageVariables){
        String pathInfo = String.valueOf(pageVariables.get("pathInfo")).substring(1);
        return Integer.parseInt(pathInfo);
    }
}
