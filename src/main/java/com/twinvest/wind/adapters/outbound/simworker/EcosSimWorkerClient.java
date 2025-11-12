package com.twinvest.wind.adapters.outbound.simworker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twinvest.wind.domain.Asset;
import com.twinvest.wind.domain.Scenario;
import com.twinvest.wind.domain.SimulationResult;
import com.twinvest.wind.ports.outbound.ArtifactStorage;
import com.twinvest.wind.ports.outbound.ExternalSimulator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;


public class EcosSimWorkerClient implements ExternalSimulator {

    private final ArtifactStorage storage;
    private final WebClient client;
    private final ObjectMapper om = new ObjectMapper();

    public EcosSimWorkerClient(ArtifactStorage storage, String baseUrl) {
        this.storage = storage;
        this.client = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public SimulationResult runWithFmu(Asset asset, Scenario sc) {
        try (InputStream is = storage.open(asset.getArtifactKey())) {
            byte[] zip = is.readAllBytes();

            Map<String, Object> scenario = new LinkedHashMap<>();
            scenario.put("nTurbines", sc.getnTurbines());
            scenario.put("pRatedKw", sc.getpRatedKw());
            scenario.put("dt_hours", sc.getDtHours());
            scenario.put("windSpeed", sc.getWindSpeed());
            scenario.put("priceEurMwh", sc.getPriceEurMwh());

            MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
            form.add("assetZip", new InputStreamResource(new ByteArrayInputStream(zip)) {
                @Override public String getFilename() { return asset.getName() + ".zip"; }
            });
            form.add("scenario", om.writeValueAsString(scenario));

            @SuppressWarnings("unchecked")
            Map<String, Object> resp = client.post().uri("/simulate-ecos")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(form))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (resp == null) {
                throw new RuntimeException("ECOS/sim-worker returned null response");
            }

            UUID id = UUID.randomUUID();
            @SuppressWarnings("unchecked")
            List<Double> time = (List<Double>) resp.get("time");
            @SuppressWarnings("unchecked")
            List<Double> powerKw = (List<Double>) resp.get("power_kw");

            Object energyObj = resp.get("total_energy_mwh");
            Object incomeObj = resp.get("total_income_eur");

            if (energyObj == null || incomeObj == null) {
                throw new RuntimeException("ECOS/sim-worker response missing required fields");
            }

            double e = ((Number) energyObj).doubleValue();
            double inc = ((Number) incomeObj).doubleValue();
            return new SimulationResult(id, asset.getId(), time, powerKw, e, inc);
        } catch (Exception e) {
            throw new RuntimeException("ECOS/sim-worker failed", e);
        }
    }
}