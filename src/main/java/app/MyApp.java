package app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.AllUsersServlet;

public class MyApp {

    public static void main(String[] args) throws Exception {
//        ResourceHandler resourceHandler= new ResourceHandler();
//        resourceHandler.setResourceBase("jcgresources");
//        resourceHandler.setDirectoriesListed(true);
//        ContextHandler contextHandler= new ContextHandler("/src/main/webapp/WEB-INF");
//        contextHandler.setHandler(resourceHandler);


        AllUsersServlet allUsersServlet = new AllUsersServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(allUsersServlet), "/all_users");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
    }
}
