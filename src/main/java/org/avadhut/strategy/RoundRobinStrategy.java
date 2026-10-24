package org.avadhut.strategy;

import org.avadhut.core.Backend;

import java.util.List;
import java.util.Set;

public class RoundRobinStrategy implements LoadBalancingStrategy{
    private int lastIndex = 0;
    private final List<Backend> backends;

    public RoundRobinStrategy(List<Backend> backends){
        this.backends =backends;
    }

    @Override
    public synchronized Backend next(Set<Backend> healthBackendSet) {
        int size = backends.size();
        int attempts = size;
        while (!healthBackendSet.contains(backends.get(lastIndex))){
            lastIndex = (lastIndex+1)%size;
            attempts--;
            if(attempts <=0)return null;
        }
        int nextIndex = lastIndex;
        lastIndex = (lastIndex+1)%size;
        return backends.get(nextIndex);
    }
}
