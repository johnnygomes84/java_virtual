package com.example.java_virtual.dto;

import lombok.Builder;

@Builder
public record BenchmarkResult(
        int requests,
        long platformThreadsMs,
        long virtualThreadsMs,
        String performanceImprovement
) {}