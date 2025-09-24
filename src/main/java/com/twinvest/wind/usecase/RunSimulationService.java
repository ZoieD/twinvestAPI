package com.twinvest.wind.usecase;

import com.twinvest.wind.domain.Asset;
import com.twinvest.wind.domain.Scenario;
import com.twinvest.wind.domain.SimulationResult;
import com.twinvest.wind.ports.inbound.GetSimulationUseCase;
import com.twinvest.wind.ports.inbound.RunSimulationUseCase;
import com.twinvest.wind.ports.outbound.AssetStore;
import com.twinvest.wind.ports.outbound.SimulationStore;

import java.util.*;
import java.util.stream.IntStream;

public class RunSimulationService implements RunSimulationUseCase, GetSimulationUseCase {
    private final AssetStore assetStore;
    private final SimulationStore simStore;

    public RunSimulationService(AssetStore assetStore, SimulationStore simStore) {
        this.assetStore = assetStore; this.simStore = simStore;
    }

    @Override
    public SimulationResult run(UUID assetId, Scenario sc) {
        Asset asset = assetStore.get(assetId).orElseThrow(() -> new NoSuchElementException("Asset not found"));
        // 简化功率曲线
        List<Double> v = sc.getWindSpeed();
        int n = v.size();
        List<Double> time = IntStream.range(0, n).mapToObj(i -> (double)i).toList();
        double[] pEach = powerCurve(v, 3.0, 12.0, 25.0, sc.getpRatedKw());
        List<Double> farmKw = new ArrayList<>(n);
        for (double d : pEach) farmKw.add(d * sc.getnTurbines());

        double energyMwh = farmKw.stream().mapToDouble(x -> x * sc.getDtHours() / 1000.0).sum();
        List<Double> price = sc.getPriceEurMwh();
        double income = 0.0;
        for (int i=0;i<n;i++) income += (farmKw.get(i)/1000.0) * price.get(i) * sc.getDtHours();

        SimulationResult result = new SimulationResult(UUID.randomUUID(), asset.getId(), time, farmKw, energyMwh, income);
        simStore.save(result);
        return result;
    }

    @Override
    public SimulationResult get(UUID simulationId) {
        return simStore.get(simulationId).orElseThrow(() -> new NoSuchElementException("Simulation not found"));
    }

    private double[] powerCurve(List<Double> v, double cutIn, double rated, double cutOut, double pRatedKw) {
        double[] out = new double[v.size()];
        for (int i=0;i<v.size();i++){
            double x = v.get(i);
            if (x < cutIn || x >= cutOut) out[i]=0.0;
            else if (x >= rated) out[i]=pRatedKw;
            else {
                double ratio = (x - cutIn) / (rated - cutIn);
                out[i] = pRatedKw * Math.pow(Math.max(0.0, ratio), 3);
            }
        }
        return out;
    }
}
