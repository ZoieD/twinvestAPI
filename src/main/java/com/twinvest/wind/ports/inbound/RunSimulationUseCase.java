package com.twinvest.wind.ports.inbound;


import com.twinvest.wind.domain.Scenario;
import com.twinvest.wind.domain.SimulationResult;
import java.util.UUID;

public interface RunSimulationUseCase {
    SimulationResult run(UUID assetId, Scenario scenario);
}
