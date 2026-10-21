package org.avadhut.strategy;

import org.avadhut.core.Backend;

import java.util.List;

public interface LoadBalancingStrategy {
    Backend next();
}
