package com.twinvest.wind.ports.inbound;

import java.util.Map;
import java.util.UUID;

public interface UploadAssetUseCase {
    UUID upload(String name, byte[] zipBytes, Map<String,Object> metadata);
}
