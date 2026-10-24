package org.avadhut.strategy;

import org.avadhut.core.Backend;

import java.util.List;
import java.util.Set;

public interface LoadBalancingStrategy {
    Backend next(Set<Backend> healthBackendsList);
}
