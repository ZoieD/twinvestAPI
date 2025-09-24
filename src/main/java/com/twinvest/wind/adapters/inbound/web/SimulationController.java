package com.twinvest.wind.adapters.inbound.web;


import com.twinvest.wind.adapters.inbound.web.dto.RunRequestDto;
import com.twinvest.wind.adapters.inbound.web.dto.SimulationDto;
import com.twinvest.wind.domain.Scenario;
import com.twinvest.wind.domain.SimulationResult;
import com.twinvest.wind.ports.inbound.GetSimulationUseCase;
import com.twinvest.wind.ports.inbound.RunSimulationUseCase;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

@RestController @RequestMapping("/api/simulations") @Validated
public class SimulationController {
    private final RunSimulationUseCase runUC;
    private final GetSimulationUseCase getUC;

    public SimulationController(RunSimulationUseCase runUC, GetSimulationUseCase getUC) {
        this.runUC = runUC; this.getUC = getUC;
    }

    @PostMapping
    public SimulationDto run(@RequestBody RunRequestDto dto){
        Scenario sc = new Scenario(dto.nTurbines(), dto.pRatedKw(), dto.dtHours(),
                dto.windSpeed(), dto.priceEurMwh());
        SimulationResult r = runUC.run(dto.assetId(), sc);
        return new SimulationDto(r.getId(), r.getAssetId(), r.getTime(), r.getPowerKw(),
                r.getTotalEnergyMwh(), r.getTotalIncomeEur());
    }

    @GetMapping("/{id}")
    public SimulationDto get(@PathVariable UUID id){
        SimulationResult r = getUC.get(id);
        return new SimulationDto(r.getId(), r.getAssetId(), r.getTime(), r.getPowerKw(),
                r.getTotalEnergyMwh(), r.getTotalIncomeEur());
    }

    @GetMapping(value="/{id}/plot", produces=MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<InputStreamResource> plot(@PathVariable UUID id) {
        SimulationResult r = getUC.get(id);
        XYChart chart = new XYChart(800, 400);
        chart.setTitle("Simulation " + id);
        chart.setXAxisTitle("t (h)");
        chart.setYAxisTitle("Power (kW)");
        chart.addSeries("Power (kW)", r.getTime(), r.getPowerKw());

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()){
            BitmapEncoder.saveBitmap(chart, bos, BitmapEncoder.BitmapFormat.PNG);
            byte[] bytes = bos.toByteArray();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .contentLength(bytes.length)
                    .body(new InputStreamResource(new ByteArrayInputStream(bytes)));
        } catch(Exception e){ throw new RuntimeException(e); }
    }
}
