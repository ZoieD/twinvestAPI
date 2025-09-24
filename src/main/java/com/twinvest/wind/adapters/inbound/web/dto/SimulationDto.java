package com.twinvest.wind.adapters.inbound.web.dto;



import java.util.List;
import java.util.UUID;

public record SimulationDto(
        UUID id, UUID assetId, List<Double> time, List<Double> powerKw,
        double totalEnergyMwh, double totalIncomeEur
) {}
