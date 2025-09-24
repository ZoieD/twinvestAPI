package com.twinvest.wind.adapters.outbound.inmemory;


import com.twinvest.wind.domain.SimulationResult;
import com.twinvest.wind.ports.outbound.SimulationStore;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySimulationStore implements SimulationStore {
    private final Map<UUID, SimulationResult> map = new ConcurrentHashMap<>();
    @Override public void save(SimulationResult r) { map.put(r.getId(), r); }
    @Override public Optional<SimulationResult> get(UUID id) { return Optional.ofNullable(map.get(id)); }
}
