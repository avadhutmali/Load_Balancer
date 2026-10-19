package org.avadhut.loadbalancer;

import java.util.List;

public class BackendPool {
    private List<Backend> backends;
    private int lastIndex = 0;

    public BackendPool(List<Backend> backends){
        this.backends = backends;
    }

    public synchronized Backend next(){
        int size = backends.size();
        int nextIndex = (lastIndex++)%size;
        return backends.get(nextIndex);
    }
}
