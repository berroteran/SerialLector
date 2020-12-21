package com.mpf.tools.zonaslector;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Java program to create a simple HTTP Server to demonstrate how to use * ServerSocket and Socket class. * * @author Javin Paul
 */
public class SimpleHTTPServer {

    public static void StartWebServer() throws IOException {
        if ( Lector.server == null ){
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

            Lector.server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
            Lector.server.createContext("/", new MyHttpHandler() );
            Lector.server.setExecutor( threadPoolExecutor );
            Lector.server.start();
            System.out.println("Server started on port 8001");
        }
    }

}

