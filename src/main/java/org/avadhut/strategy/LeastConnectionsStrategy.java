package org.avadhut.strategy;

import org.avadhut.core.Backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LeastConnectionsStrategy implements LoadBalancingStrategy, ConnectionTrackingStrategy {
    private final Map<Backend, AtomicInteger> connectionTrackingMap;

    public LeastConnectionsStrategy(List<Backend> backendList){
        this.connectionTrackingMap = new ConcurrentHashMap<>();
        for(Backend backend : backendList){
            connectionTrackingMap.put(backend,new AtomicInteger(0));
        }
    }

    @Override
    public synchronized void incrementConnections(Backend backend) {
        connectionTrackingMap.get(backend).incrementAndGet();
    }

    @Override
    public synchronized void decrementConnections(Backend backend) {
        if(connectionTrackingMap.get(backend).get() <0){
            return;
        }
        connectionTrackingMap.get(backend).decrementAndGet();
    }

    @Override
    public synchronized Backend next() {
        Backend nextBackend = null;
        int min = Integer.MAX_VALUE;

        for(Backend backend : connectionTrackingMap.keySet()){
            int val = connectionTrackingMap.get(backend).get();
            if(val < min){
                min = val;
                nextBackend = backend;
            }
        }
        incrementConnections(nextBackend);
        System.out.println("Selecting: " + nextBackend.port() + " | state: " + connectionTrackingMap);
        return  nextBackend;
    }
}
