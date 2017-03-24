package com.miskevich.servlets;

import com.miskevich.beans.User;
import com.miskevich.db.SQLHelper;
import com.miskevich.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.miskevich.app.MyApp.pooledConnectionServlet;

public class AddUserServlet extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {

        Map<String, Object> pageVariables = createPageVariablesMap(request);
        pageVariables.put("message", "");

        response.getWriter().println(PageGenerator.instance().getPage("add_user.html", pageVariables));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(request);

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String salary = request.getParameter("salary");
        String dateOfBirth = request.getParameter("dateOfBirth");

        response.setContentType("text/html;charset=utf-8");

        if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty() || dateOfBirth == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        java.util.Date date;
        try {
            date = df.parse(dateOfBirth);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        java.sql.Date sqlStartDate = new Date(date.getTime());
        System.out.println(sqlStartDate);
        pageVariables.put("firstName", firstName == null ? "" : firstName);
        pageVariables.put("lastName", lastName == null ? "" : lastName);
        pageVariables.put("salary", Double.valueOf(salary));
        pageVariables.put("dateOfBirth", sqlStartDate);

        System.out.println(pageVariables);

        response.getWriter().println(PageGenerator.instance().getPage("all_users.html", pageVariables));
    }

    private Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        List<User> users = SQLHelper.getAllUsers(pooledConnectionServlet);
        pageVariables.put("users", users);
        return pageVariables;
    }
}
