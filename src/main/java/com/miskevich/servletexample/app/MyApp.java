package com.miskevich.servletexample.app;

import com.miskevich.servletexample.db.DBHelper;
import com.miskevich.servletexample.servlets.AddUserServlet;
import com.miskevich.servletexample.servlets.AllUsersServlet;
import com.miskevich.servletexample.servlets.EditUserServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.sql.PooledConnection;


public class MyApp {

    public static PooledConnection pooledConnectionServlet;

    public static void main(String[] args) throws Exception {

        DBHelper dbHelper = new DBHelper();
        String connectionPath = dbHelper.connectionMap.get("SERVLET");
        pooledConnectionServlet = dbHelper.getConnectionPool(connectionPath);

        AllUsersServlet allUsersServlet = new AllUsersServlet();
        AddUserServlet addUserServlet = new AddUserServlet();
        EditUserServlet editUserServlet = new EditUserServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        String pwdPath = System.getProperty("user.dir");
        context.setResourceBase(pwdPath + "/templates");
        context.setContextPath("/");
        context.addServlet(new ServletHolder(allUsersServlet), "/user/all");
        context.addServlet(new ServletHolder(addUserServlet), "/user/add");
        context.addServlet(new ServletHolder(editUserServlet), "/user/edit/*");

        //ServletContextHandler should have a DefaultServlet added to its servlet tree
        // (this is what actually serves static resources)
        ServletHolder holderPwd = new ServletHolder("default", DefaultServlet.class);
        holderPwd.setInitParameter("dirAllowed","true");
        context.addServlet(holderPwd,"/");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
    }
}
