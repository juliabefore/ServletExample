package com.miskevich.servlets;

import com.miskevich.beans.User;
import com.miskevich.db.SQLHelper;
import com.miskevich.templater.PageGenerator;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.miskevich.app.MyApp.pooledConnection;

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
        SQLHelper sqlHelper = new SQLHelper();
        List<User> users = sqlHelper.getAllUsers(pooledConnection);
        pageVariables.put("users", users);
        return pageVariables;
    }
}
