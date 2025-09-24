package com.twinvest.wind.ports.outbound;

import com.twinvest.wind.domain.Asset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssetStore {
    Asset save(Asset asset);
    Optional<Asset> get(UUID id);
    List<Asset> list();
}
