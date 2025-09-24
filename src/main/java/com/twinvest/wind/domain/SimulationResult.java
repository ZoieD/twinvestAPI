package com.twinvest.wind.domain;

import java.util.List;
import java.util.UUID;

public class SimulationResult {
    private final UUID id;
    private final UUID assetId;
    private final List<Double> time;
    private final List<Double> powerKw;
    private final double totalEnergyMwh;
    private final double totalIncomeEur;

    public SimulationResult(UUID id, UUID assetId, List<Double> time, List<Double> powerKw,
                            double totalEnergyMwh, double totalIncomeEur) {
        this.id = id; this.assetId = assetId; this.time = time; this.powerKw = powerKw;
        this.totalEnergyMwh = totalEnergyMwh; this.totalIncomeEur = totalIncomeEur;
    }
    public UUID getId(){ return id; }
    public UUID getAssetId(){ return assetId; }
    public List<Double> getTime(){ return time; }
    public List<Double> getPowerKw(){ return powerKw; }
    public double getTotalEnergyMwh(){ return totalEnergyMwh; }
    public double getTotalIncomeEur(){ return totalIncomeEur; }
}
