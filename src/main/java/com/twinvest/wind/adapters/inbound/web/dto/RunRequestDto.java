package com.twinvest.wind.adapters.inbound.web.dto;

import jakarta.validation.constraints.*;
import java.util.List;
import java.util.UUID;

public record RunRequestDto(
        @NotNull UUID assetId,
        @Min(1) int nTurbines,
        @Positive double pRatedKw,
        @Positive double dtHours,
        @NotEmpty List<Double> windSpeed,
        @NotEmpty List<Double> priceEurMwh
) {}
