package org.avadhut.strategy;

import org.avadhut.core.Backend;

public interface ConnectionTrackingStrategy {
    void incrementConnections(Backend backend);
    void decrementConnections(Backend backend);
}
