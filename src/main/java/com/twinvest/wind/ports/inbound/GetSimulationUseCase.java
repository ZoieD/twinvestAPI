package com.twinvest.wind.ports.inbound;


import com.twinvest.wind.domain.SimulationResult;
import java.util.UUID;

public interface GetSimulationUseCase {
    SimulationResult get(UUID simulationId);
}
