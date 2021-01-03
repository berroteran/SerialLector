package com.mpf.tools.zonaslector;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MyHttpHandler implements HttpHandler {
    private static final String HEADER_ALLOW = "Allow";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private static final int STATUS_OK = 200;
    private static final int STATUS_METHOD_NOT_ALLOWED = 405;

    private static final int NO_RESPONSE_LENGTH = -1;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue=null;

        if("GET".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handleGetRequest( httpExchange );
        }else if("POST".equals(httpExchange)) {
            requestParamValue = handlePostRequest(httpExchange);
        }
        //
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        if (httpExchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
            httpExchange.sendResponseHeaders(204, -1);
            return;
        }
        httpExchange.getResponseHeaders().add( HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
        handleResponse(httpExchange,requestParamValue);
    }

    private String handlePostRequest(HttpExchange httpExchange) {

        return httpExchange.
                getRequestURI()
                .toString()
                .split("\\?")[1]
                .split("=")[1];
    }

    private String handleGetRequest(HttpExchange httpExchange) {
        String par  = httpExchange.
        getRequestURI()
                .toString()
                .split("\\?")[1]
                .split("=")[1];
        String valor = "";
        System.out.println("valor parametro: " + par);
        if ( par.equals("b")){
            valor =  Lector.getBascula();
        }else{
            valor = Lector.getHumedad();
        }
        return valor;
    }

    private void handleResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {
        requestParamValue = requestParamValue == null ? "" : requestParamValue;
        OutputStream outputStream = httpExchange.getResponseBody();
        StringBuilder htmlBuilder = new StringBuilder();

        String cadena = "{\"status\":\"OK\", \"data\" : [ ";
            cadena += "{\"port1\":\"" + Lector.getBascula() + "\"} ";
            cadena += "{\"port2\":\"" + Lector.getHumedad() + "\"} ";
            cadena += "]}";
        
        htmlBuilder.append(cadena);
        // encode HTML content
        String htmlResponse = htmlBuilder.toString();
        // this line is a must
        httpExchange.sendResponseHeaders(200, htmlResponse.length());

        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}