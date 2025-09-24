package com.twinvest.wind.ports.outbound;


import java.io.InputStream;

public interface ArtifactStorage {
    String save(String originalName, InputStream data);   // 返回对象key
    InputStream open(String objectKey);
}
