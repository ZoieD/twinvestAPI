package com.twinvest.wind.ports.outbound;


import com.twinvest.wind.domain.SimulationResult;
import java.util.Optional;
import java.util.UUID;

public interface SimulationStore {
    void save(SimulationResult result);
    Optional<SimulationResult> get(UUID id);
}
