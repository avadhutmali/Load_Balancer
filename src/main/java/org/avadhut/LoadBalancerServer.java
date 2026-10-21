package org.avadhut;

import com.sun.net.httpserver.HttpServer;
import org.avadhut.core.Backend;
import org.avadhut.core.BackendPool;
import org.avadhut.core.ProxyHandler;
import org.avadhut.core.WeightedBackend;
import org.avadhut.strategy.RoundRobinStrategy;
import org.avadhut.strategy.WeightedRoundRobinStrategy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Executors;

public class LoadBalancerServer {

    public static void main(String[] args) throws IOException {
        Backend backend1 = new Backend("localhost",8081);
        Backend backend2 = new Backend("localhost",8082);
        Backend backend3 = new Backend("localhost",8083);

        WeightedBackend weightedBackend1 = new WeightedBackend(backend1,3);
        WeightedBackend weightedBackend2 = new WeightedBackend(backend2,1);
        WeightedBackend weightedBackend3 = new WeightedBackend(backend3,1);

        List<WeightedBackend> weightedBackendList = List.of(weightedBackend1,weightedBackend2,weightedBackend3);
        BackendPool pool = new BackendPool(new WeightedRoundRobinStrategy(weightedBackendList));

//        List<Backend> backendList = List.of(backend1,backend2,backend3);
//        BackendPool pool = new BackendPool(new RoundRobinStrategy(backendList));

        ProxyHandler handler = new ProxyHandler(pool);
        HttpServer server = HttpServer.create(new InetSocketAddress(9090),0);

        server.createContext("/",handler);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        server.start();

        System.out.println("Load Balancer server started on the port 9090");
    }
}
