package com.mpf.tools.webserver;

import com.mpf.tools.zonaslector.Lector;
import com.sun.net.httpserver.HttpServer;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Java program to create a simple HTTP Server to demonstrate how to use * ServerSocket and Socket class. * * @author Javin Paul
 */
public class SimpleHTTPServer {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 8001;
    private static final int BACKLOG = 1;

    public static void StartWebServer() throws IOException {
        if ( Lector.server == null ){
            try {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

                Lector.server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);
                Lector.server.createContext("/", new MyHttpHandler());
                Lector.server.setExecutor(threadPoolExecutor);
                Lector.server.start();
                System.out.println("Server started on port:" + PORT);
            }catch (Exception e){
                throw new EOFException("No se pudo iniciar servicio. "  +e.getMessage());
            }
        }
    }
    
    public static void stopServiceHTT(){
        Lector.server.stop(2);
        Lector.server = null;
    }

}

