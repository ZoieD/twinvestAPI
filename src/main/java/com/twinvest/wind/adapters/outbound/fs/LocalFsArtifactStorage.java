package com.twinvest.wind.adapters.outbound.fs;


import com.twinvest.wind.ports.outbound.ArtifactStorage;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;

public class LocalFsArtifactStorage implements ArtifactStorage {
    private final Path baseDir;

    public LocalFsArtifactStorage(String baseDir) {
        this.baseDir = Paths.get(baseDir);
        try { Files.createDirectories(this.baseDir); } catch (IOException ignored) {}
    }

    @Override
    public String save(String originalName, InputStream data) {
        String key = UUID.randomUUID() + "-" + originalName;
        Path p = baseDir.resolve(key);
        try (OutputStream out = Files.newOutputStream(p)) {
            data.transferTo(out);
        } catch (IOException e) { throw new RuntimeException(e); }
        return key;
    }

    @Override
    public InputStream open(String objectKey) {
        Path p = baseDir.resolve(objectKey);
        try { return Files.newInputStream(p); } catch (IOException e) { throw new RuntimeException(e); }
    }
}
