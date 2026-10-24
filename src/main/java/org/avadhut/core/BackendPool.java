package org.avadhut.core;

import org.avadhut.strategy.ConnectionTrackingStrategy;
import org.avadhut.strategy.LoadBalancingStrategy;

import java.util.List;
import java.util.Set;

public class BackendPool {
    private final LoadBalancingStrategy strategy;
    private final HealthChecker checker;
    public BackendPool(LoadBalancingStrategy strategy, HealthChecker checker){
        this.strategy = strategy;
        this.checker = checker;
    }

    public Backend next(){
        Set<Backend> healthyBackendList = checker.getHealthyBackends();
        return strategy.next(healthyBackendList);
    }

    public void release(Backend backend){
        if(strategy instanceof ConnectionTrackingStrategy tracker){
            tracker.decrementConnections(backend);
        }
    }
}
