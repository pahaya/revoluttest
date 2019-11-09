package ru.pahaya;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import ru.pahaya.entrypoints.AccountEntryPoint;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import ru.pahaya.entrypoints.TransactionEntryPoint;

/**
 * This is main entry point of the program
 */
public class Application {

    private static final Logger logger = LogManager.getLogger(Application.class);
    private volatile static Server server;

    public static void main(String[] args) {
        startServer();
        try {
            server.join();
        } catch (InterruptedException e) {
            logger.error("Join failed!");
        }
    }

    /**
     * Start jetty server with jersey
     */
    public static void startServer() {
        //Create the server
        int maxThreads = 100;
        int minThreads = 10;
        int idleTimeout = 120;

        QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);

        server = new Server(threadPool);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                TransactionEntryPoint.class.getCanonicalName() + ";" +
                        AccountEntryPoint.class.getCanonicalName());

        server.setHandler(context);
        try {
            server.start();
            server.dump(System.err);

        } catch (Exception e) {
            logger.error("Error during start of the server.", e);
        }
    }

    /**
     * Stop Jetty
     */
    public static void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            logger.error("Error during stop of the server!", e);
        }
        ;
    }
}
