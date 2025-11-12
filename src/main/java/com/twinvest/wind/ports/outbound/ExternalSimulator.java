package com.twinvest.wind.ports.outbound;

import com.twinvest.wind.domain.Asset;
import com.twinvest.wind.domain.Scenario;
import com.twinvest.wind.domain.SimulationResult;


public interface ExternalSimulator {
    SimulationResult runWithFmu(Asset asset, Scenario scenario);
}
