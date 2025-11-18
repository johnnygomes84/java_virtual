package com.example.java_virtual.dto;

import lombok.Builder;

@Builder
record BenchmarkResult(
        int requests,
        long platformThreadsMs,
        long virtualThreadsMs,
        String performanceImprovement
) {}