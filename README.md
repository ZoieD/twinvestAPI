twinvest-api/
├─ pom.xml
├─ src/main/java/com/twinvest/wind/
│  ├─ Application.java
│  ├─ domain/                     # 领域模型（纯业务对象）
│  │  ├─ Asset.java
│  │  ├─ Scenario.java
│  │  └─ SimulationResult.java
│  ├─ ports/                      # 端口（接口，定义用例与外部依赖）
│  │  ├─ in/
│  │  │  ├─ ListAssetsUseCase.java
│  │  │  ├─ UploadAssetUseCase.java
│  │  │  ├─ RunSimulationUseCase.java
│  │  │  └─ GetSimulationUseCase.java
│  │  └─ out/
│  │     ├─ AssetStore.java
│  │     ├─ ArtifactStorage.java
│  │     └─ SimulationStore.java
│  ├─ usecase/                    # 用例实现（核心编排，只依赖端口）
│  │  ├─ AssetCatalogService.java
│  │  └─ RunSimulationService.java
│  ├─ adapters/                   # 适配器（技术细节在外层）
│  │  ├─ in/web/                  # 入站适配器：HTTP 控制器/DTO
│  │  │  ├─ AssetController.java
│  │  │  ├─ SimulationController.java
│  │  │  └─ dto/
│  │  │     ├─ AssetDto.java
│  │  │     ├─ RunRequestDto.java
│  │  │     └─ SimulationDto.java
│  │  └─ out/
│  │     ├─ inmemory/
│  │     │  ├─ InMemoryAssetStore.java
│  │     │  └─ InMemorySimulationStore.java
│  │     └─ fs/
│  │        └─ LocalFsArtifactStorage.java
│  └─ config/
│     └─ BeanConfig.java          # 组装：把端口实现（适配器）注入用例
└─ src/main/resources/
└─ application.yml
