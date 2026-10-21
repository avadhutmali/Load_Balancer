package org.avadhut.core;

import org.avadhut.strategy.LoadBalancingStrategy;

import java.util.List;

public class BackendPool {
    private final LoadBalancingStrategy strategy;
    public BackendPool(LoadBalancingStrategy strategy){
        this.strategy = strategy;
    }

    public Backend next(){
        return strategy.next();
    }
}
