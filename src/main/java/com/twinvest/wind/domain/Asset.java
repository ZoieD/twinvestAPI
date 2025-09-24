// domain/Asset.java
package com.twinvest.wind.domain;

import java.util.Map;
import java.util.UUID;

public class Asset {
    private final UUID id;
    private final String name;
    private final String artifactKey;   // 外部存储的对象key（zip）
    private final Map<String, Object> metadata;

    public Asset(UUID id, String name, String artifactKey, Map<String, Object> metadata) {
        this.id = id; this.name = name; this.artifactKey = artifactKey; this.metadata = metadata;
    }
    public UUID getId(){ return id; }
    public String getName(){ return name; }
    public String getArtifactKey(){ return artifactKey; }
    public Map<String,Object> getMetadata(){ return metadata; }
}
