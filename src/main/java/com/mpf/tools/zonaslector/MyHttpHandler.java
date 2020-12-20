package com.mpf.tools.zonaslector;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.lang3.StringEscapeUtils;

public class MyHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue=null;

        if("GET".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handleGetRequest( httpExchange );
        }else if("POST".equals(httpExchange)) {
            requestParamValue = handlePostRequest(httpExchange);
        }
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
            valor =  "200.00";
        }else{
            valor = "300.00";
        }
        return valor;
    }

    private void handleResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {
        requestParamValue = requestParamValue == null ? "" : requestParamValue;
        OutputStream outputStream = httpExchange.getResponseBody();
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html>").
        append("<body>").
        append("<h1>").
            append("Hello ")
                .append(requestParamValue)
                .append("</h1>")
                .append("</body>")
                .append("</html>");
        // encode HTML content
        String htmlResponse = htmlBuilder.toString();
        // this line is a must
        httpExchange.sendResponseHeaders(200, htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}