package com.twinvest.wind.usecase;

import com.twinvest.wind.domain.Asset;
import com.twinvest.wind.domain.Scenario;
import com.twinvest.wind.domain.SimulationResult;
import com.twinvest.wind.ports.inbound.GetSimulationUseCase;
import com.twinvest.wind.ports.inbound.RunSimulationUseCase;
import com.twinvest.wind.ports.outbound.AssetStore;
import com.twinvest.wind.ports.outbound.ExternalSimulator;
import com.twinvest.wind.ports.outbound.SimulationStore;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

public class RunSimulationService implements RunSimulationUseCase, GetSimulationUseCase {
    private final AssetStore assetStore;
    private final SimulationStore simStore;
    private final ExternalSimulator external; //can be null
    public RunSimulationService(AssetStore assetStore, SimulationStore simStore, ExternalSimulator external) {
        this.assetStore = assetStore; this.simStore = simStore; this.external = external;
    }

    @Override
    public SimulationResult run(UUID assetId, Scenario sc) {
        Asset asset = assetStore.get(assetId).orElseThrow(() -> new NoSuchElementException("Asset not found"));

        // Plan A: if sim-worker activated (external !=null)
        if (external !=null) {
            SimulationResult result = external.runWithFmu(asset, sc);
            simStore.save(result);
            return result;
        }

        // Plan B: if it's null, runByPowerCurve
        SimulationResult result = runByPowerCurve(asset, sc);
        simStore.save(result);
        return result;
    }

    // -------- Internal Power Curve（MVP）---------
    private SimulationResult runByPowerCurve(Asset asset, Scenario sc) {
        List<Double> wind = sc.getWindSpeed();
        List<Double> price = sc.getPriceEurMwh();
        int n = wind.size();
        if (price != null && price.size() != n) {
            throw new IllegalArgumentException("priceEurMwh length != windSpeed length");
        }

        List<Double> time = IntStream.range(0, n)
                .mapToObj(i -> (double) i)
                .collect(Collectors.toList());

        // cut-in/rated/cut-out can be set in the setting；put some defualt value first
        double vCutIn = 3.0, vRated = 12.0, vCutOut = 25.0;
        double[] singleKw = powerCurve(wind, vCutIn, vRated, vCutOut, sc.getpRatedKw());

        List<Double> farmKw = new ArrayList<>(n);
        for (double d : singleKw) {
            farmKw.add(d * sc.getnTurbines());
        }

        double dt = sc.getDtHours();
        double energyMwh = farmKw.stream().mapToDouble(x -> x * dt / 1000.0).sum();

        double income = 0.0;
        if (price != null) {
            for (int i = 0; i < n; i++) {
                income += (farmKw.get(i) / 1000.0) * price.get(i) * dt;
            }
        }

        return new SimulationResult(
                UUID.randomUUID(),
                asset.getId(),
                time,
                farmKw,
                energyMwh,
                income
        );
    }

    @Override
    public SimulationResult get(UUID simulationId) {
        return simStore.get(simulationId).orElseThrow(() -> new NoSuchElementException("Simulation not found"));
    }

    // simplified power curve：cut-in 到 rated 用三次爬升，rated 到 cut-out 恒定
    private double[] powerCurve(List<Double> wind, double vCutIn, double vRated, double vCutOut, double pRatedKw) {
        int n = wind.size();
        double[] p = new double[n];
        for (int i = 0; i < n; i++) {
            double v = wind.get(i);
            double val;
            if (v < vCutIn || v >= vCutOut) {
                val = 0.0;
            } else if (v >= vRated) {
                val = pRatedKw;
            } else {
                double x = (v - vCutIn) / (vRated - vCutIn);
                val = pRatedKw * x * x * x;
            }
            p[i] = val;
        }
        return p;
    }





}
