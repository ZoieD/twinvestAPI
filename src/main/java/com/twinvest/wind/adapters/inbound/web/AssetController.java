package com.twinvest.wind.adapters.inbound.web;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twinvest.wind.adapters.inbound.web.dto.AssetDto;
import com.twinvest.wind.domain.Asset;
import com.twinvest.wind.ports.inbound.ListAssetsUseCase;
import com.twinvest.wind.ports.inbound.UploadAssetUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assets")
public class AssetController {
    private final ListAssetsUseCase listUC;
    private final UploadAssetUseCase uploadUC;
    private final ObjectMapper om = new ObjectMapper();

    public AssetController(
            @Qualifier("listAssetsUseCase") ListAssetsUseCase listUC,
           @Qualifier("uploadAssetUseCase") UploadAssetUseCase uploadUC) {
        this.listUC = listUC; this.uploadUC = uploadUC;
    }

    @GetMapping
    public List<AssetDto> list(){
        return listUC.listAssets().stream()
                .map(a -> new AssetDto(a.getId(), a.getName(), a.getMetadata()))
                .collect(Collectors.toList());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String,Object> upload(@RequestParam("file") MultipartFile file,
                                     @RequestParam("name") String name,
                                     @RequestParam(value="metadata", required=false) String metadataJson) throws Exception {
        Map<String,Object> meta = metadataJson==null? Map.of()
                : om.readValue(metadataJson, new TypeReference<Map<String,Object>>(){});
        byte[] bytes = file.getBytes();
        UUID id = uploadUC.upload(name, bytes, meta);
        return Map.of("id", id, "name", name);
    }
}
