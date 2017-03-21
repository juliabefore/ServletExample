package app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.AddUserServlet;
import servlets.AllUsersServlet;
import servlets.EditUserServlet;

public class MyApp {

    public static void main(String[] args) throws Exception {

        AllUsersServlet allUsersServlet = new AllUsersServlet();
        AddUserServlet addUserServlet = new AddUserServlet();
        EditUserServlet editUserServlet = new EditUserServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        String pwdPath = System.getProperty("user.dir");
        context.setResourceBase(pwdPath + "/templates");
        context.setContextPath("/");
        context.addServlet(new ServletHolder(allUsersServlet), "/all_users");
        context.addServlet(new ServletHolder(addUserServlet), "/add_user");
        context.addServlet(new ServletHolder(editUserServlet), "/edit_user");

        //ServletContextHandler should have a DefaultServlet added to its servlet tree
        // (this is what actually serves static resources)
        ServletHolder holderPwd = new ServletHolder("default",DefaultServlet.class);
        holderPwd.setInitParameter("dirAllowed","true");
        context.addServlet(holderPwd,"/");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
    }
}
