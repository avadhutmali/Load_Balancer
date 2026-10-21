package org.avadhut.core;

public class WeightedBackend {
    private Backend backend;
    private int weight;

    public WeightedBackend(Backend backend,int weight){
        this.backend = backend;
        this.weight = weight;
    }

    public Backend getBackend() {
        return backend;
    }

    public int getWeight() {
        return weight;
    }
}
