package org.avadhut.core;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.avadhut.strategy.LoadBalancingStrategy;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ProxyHandler implements HttpHandler {

    private final BackendPool pool;
    private final HttpClient client;

    public ProxyHandler(BackendPool pool){
        this.client = HttpClient.newHttpClient();
        this.pool = pool;
    }
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Backend backend = pool.next();
        String host = backend.host();
        int port = backend.port();


        String uriString = "http://"+host+":"+port+httpExchange.getRequestURI().getPath();
        URI uri = URI.create(uriString);

        String method = httpExchange.getRequestMethod();
        System.out.println("HTTP method: "+method);
        try {

            HttpRequest.BodyPublisher  publisher;
            if("GET".equalsIgnoreCase(httpExchange.getRequestMethod()) || "DELETE".equalsIgnoreCase(httpExchange.getRequestMethod())){
                publisher = HttpRequest.BodyPublishers.noBody();
            }else publisher = HttpRequest.BodyPublishers.ofInputStream(()->httpExchange.getRequestBody());


            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .method(method,publisher)
                    .build();
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            httpExchange.sendResponseHeaders(response.statusCode(),response.body().length);
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.body());
            os.close();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            pool.release(backend);
        }
    }
}
