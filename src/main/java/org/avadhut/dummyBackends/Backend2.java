package org.avadhut.dummyBackends;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Backend2 {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8082),0);
//        try {
//            Thread.sleep(1000); // simulate slow backend
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
        server.createContext("/",exchange->{
            String responseString = "Response From server 8082 \n";
            exchange.sendResponseHeaders(200,responseString.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(responseString.getBytes());
            os.close();
        });
        server.setExecutor(null);
        server.start();
        System.out.println("\n Server is started on the port 8082 \n");


    }
}
