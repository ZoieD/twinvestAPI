package com.twinvest.wind.adapters.outbound.inmemory;


import com.twinvest.wind.domain.Asset;
import com.twinvest.wind.ports.outbound.AssetStore;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAssetStore implements AssetStore {
    private final Map<UUID, Asset> map = new ConcurrentHashMap<>();
    @Override public Asset save(Asset asset) { map.put(asset.getId(), asset); return asset; }
    @Override public Optional<Asset> get(UUID id) { return Optional.ofNullable(map.get(id)); }
    @Override public List<Asset> list() { return new ArrayList<>(map.values()); }
}
