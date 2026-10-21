package org.avadhut.strategy;

import org.avadhut.core.Backend;
import org.avadhut.core.WeightedBackend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeightedRoundRobinStrategy implements LoadBalancingStrategy{
    private final List<WeightedBackend> weightedBackends;
    private final Map<Backend,Integer> effectiveWeightMap;
    private final Map<Backend,Integer> currentWeightMap;

    private int totalWeight =0;

    public WeightedRoundRobinStrategy(List<WeightedBackend> weightedBackends){
        this.weightedBackends = weightedBackends;
        effectiveWeightMap = new HashMap<>();
        currentWeightMap = new HashMap<>();

        for(WeightedBackend weightedBackend : weightedBackends){
            effectiveWeightMap.put(weightedBackend.getBackend(),weightedBackend.getWeight());
            currentWeightMap.put(weightedBackend.getBackend(),0);
            totalWeight += weightedBackend.getWeight();
        }
    }

    @Override
    public synchronized Backend next() {
        Backend nextBackend = null;
        int maxWeight =Integer.MIN_VALUE;

        for(WeightedBackend weightedBackend: weightedBackends){
            int currentWeight = currentWeightMap.get(weightedBackend.getBackend());
            int effectiveWeight = effectiveWeightMap.get(weightedBackend.getBackend());

            currentWeight+= effectiveWeight;
            if(currentWeight > maxWeight){
                maxWeight = currentWeight;
                nextBackend = weightedBackend.getBackend();
            }
            currentWeightMap.put(weightedBackend.getBackend(),currentWeight);
        }
        currentWeightMap.put(nextBackend,currentWeightMap.get(nextBackend) - totalWeight);

        return nextBackend;
    }
}
