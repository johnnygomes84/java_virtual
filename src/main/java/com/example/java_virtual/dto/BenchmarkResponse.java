package com.example.java_virtual.dto;

import lombok.Builder;

@Builder
public record BenchmarkResponse(
        int requests,
        long platformThreadsMs,
        long virtualThreadsMs,
        String performanceImprovement
) {}
