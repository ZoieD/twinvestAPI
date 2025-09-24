package com.twinvest.wind.usecase;

import com.twinvest.wind.domain.Asset;
import com.twinvest.wind.ports.inbound.ListAssetsUseCase;
import com.twinvest.wind.ports.inbound.UploadAssetUseCase;
import com.twinvest.wind.ports.outbound.ArtifactStorage;
import com.twinvest.wind.ports.outbound.AssetStore;

import java.io.ByteArrayInputStream;
import java.util.*;

public class AssetCatalogService implements ListAssetsUseCase, UploadAssetUseCase {
    private final AssetStore assetStore;
    private final ArtifactStorage artifactStorage;

    public AssetCatalogService(AssetStore assetStore, ArtifactStorage artifactStorage) {
        this.assetStore = assetStore; this.artifactStorage = artifactStorage;
    }

    @Override
    public List<Asset> listAssets() { return assetStore.list(); }

    @Override
    public UUID upload(String name, byte[] zipBytes, Map<String, Object> metadata) {
        String key = artifactStorage.save(name + ".zip", new ByteArrayInputStream(zipBytes));
        Asset asset = new Asset(UUID.randomUUID(), name, key, metadata==null? Map.of(): metadata);
        assetStore.save(asset);
        return asset.getId();
    }
}
