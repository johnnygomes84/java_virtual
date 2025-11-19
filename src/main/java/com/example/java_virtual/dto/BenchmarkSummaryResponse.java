package com.example.java_virtual.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record BenchmarkSummaryResponse(
        List<BenchmarkResult> benchmarks,
        String conclusion
) {}
