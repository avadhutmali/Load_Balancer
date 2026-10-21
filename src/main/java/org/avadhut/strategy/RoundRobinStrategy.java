package org.avadhut.strategy;

import org.avadhut.core.Backend;

import java.util.List;

public class RoundRobinStrategy implements LoadBalancingStrategy{
    private int lastIndex = 0;
    private final List<Backend> backends;

    public RoundRobinStrategy(List<Backend> backends){
        this.backends =backends;
    }

    @Override
    public synchronized Backend next() {
        int size = backends.size();
        int nextIndex = (lastIndex++)%size;
        return backends.get(nextIndex);
    }
}
