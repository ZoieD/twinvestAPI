package com.twinvest.wind.adapters.inbound.web.dto;



import java.util.Map;
import java.util.UUID;

public record AssetDto(UUID id, String name, Map<String,Object> metadata) {}
