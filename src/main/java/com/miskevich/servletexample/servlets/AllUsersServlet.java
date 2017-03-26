package com.miskevich.servletexample.servlets;

import com.miskevich.servletexample.entity.User;
import com.miskevich.servletexample.db.core.SQLHelper;
import com.miskevich.servletexample.templater.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.miskevich.servletexample.app.MyApp.pooledConnectionServlet;

public class AllUsersServlet extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {

        Map<String, Object> pageVariables = createPageVariablesMap();
        pageVariables.put("message", "");

        response.getWriter().println(PageGenerator.instance().getPage("all_users.html", pageVariables));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

    }

    private Map<String, Object> createPageVariablesMap() {
        Map<String, Object> pageVariables = new HashMap<>();
        List<User> users = SQLHelper.getAllUsers(pooledConnectionServlet);
        pageVariables.put("users", users);
        return pageVariables;
    }
}
