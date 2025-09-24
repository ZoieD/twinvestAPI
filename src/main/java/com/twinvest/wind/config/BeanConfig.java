package com.twinvest.wind.config;

import com.twinvest.wind.adapters.outbound.fs.LocalFsArtifactStorage;
import com.twinvest.wind.adapters.outbound.inmemory.InMemoryAssetStore;
import com.twinvest.wind.adapters.outbound.inmemory.InMemorySimulationStore;
import com.twinvest.wind.ports.outbound.ArtifactStorage;
import com.twinvest.wind.ports.outbound.AssetStore;
import com.twinvest.wind.ports.outbound.SimulationStore;
import com.twinvest.wind.ports.inbound.*;
import com.twinvest.wind.usecase.AssetCatalogService;
import com.twinvest.wind.usecase.RunSimulationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public ArtifactStorage artifactStorage(
            @Value("${twinvest.storage.baseDir:./data/artifacts}") String baseDir) {

        try {
            var path = java.nio.file.Paths.get(baseDir).toAbsolutePath();
            java.nio.file.Files.createDirectories(path);   // ensure exists and writable
            return new LocalFsArtifactStorage(path.toString());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to init ArtifactStorage at " + baseDir, e);
        }
    }

    @Bean public AssetStore assetStore(){ return new InMemoryAssetStore(); }
    @Bean public SimulationStore simulationStore(){ return new InMemorySimulationStore(); }

    // 用例实现（入站端口的实现）
    @Bean
    public UploadAssetUseCase uploadAssetUseCase(AssetStore assetStore, ArtifactStorage artifactStorage){
        return new AssetCatalogService(assetStore, artifactStorage);
    }

    @Bean
    public ListAssetsUseCase listAssetsUseCase(AssetStore assetStore, ArtifactStorage artifactStorage){
        return new AssetCatalogService(assetStore, artifactStorage);
    }

    @Bean
    public RunSimulationUseCase runSimulationUseCase(AssetStore assetStore, SimulationStore simStore){
        return new RunSimulationService(assetStore, simStore);
    }

    @Bean
    public GetSimulationUseCase getSimulationUseCase(AssetStore assetStore, SimulationStore simStore){
        return new RunSimulationService(assetStore, simStore);
    }
}
