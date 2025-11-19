package com.example.java_virtual.service;

import com.example.java_virtual.config.BenchmarkConfig;
import com.example.java_virtual.dto.BenchmarkResponse;
import com.example.java_virtual.dto.BenchmarkResult;
import com.example.java_virtual.dto.BenchmarkSummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BenchmarkService {

    private final ThreadBenchmarkService threadBenchmarkService;
    private final BenchmarkConfig config;

    public BenchmarkResponse runBenchmark(int requests) {
        log.info("Starting benchmark with {} requests", requests);
        validateRequestCount(requests);

        var result = executeAndBuildResult(requests);
        return convertToResponse(result);
    }

    public BenchmarkSummaryResponse runSummaryBenchmark(List<Integer> requestCounts) {
        var benchmarks = requestCounts.stream()
                .map(this::executeAndBuildResult)
                .toList();

        var conclusion = generateConclusion(benchmarks);

        return BenchmarkSummaryResponse.builder()
                .benchmarks(benchmarks)
                .conclusion(conclusion)
                .build();
    }

    private BenchmarkResult executeAndBuildResult(int requests) {
        var platformTime = threadBenchmarkService.runWithPlatformThreads(
                requests, config.getPlatformThreadPoolSize());
        var virtualTime = threadBenchmarkService.runWithVirtualThreads(requests);

        return BenchmarkResult.builder()
                .requests(requests)
                .platformThreadsMs(platformTime)
                .virtualThreadsMs(virtualTime)
                .performanceImprovement(calculateImprovement(platformTime, virtualTime))
                .build();
    }

    private BenchmarkResponse convertToResponse(BenchmarkResult result) {
        return BenchmarkResponse.builder()
                .requests(result.requests())
                .platformThreadsMs(result.platformThreadsMs())
                .virtualThreadsMs(result.virtualThreadsMs())
                .performanceImprovement(result.performanceImprovement())
                .build();
    }

    private String calculateImprovement(long platformTime, long virtualTime) {
        if (platformTime <= 0 || virtualTime <= 0) {
            return "N/A";
        }
        var improvement = ((double) (platformTime - virtualTime) / platformTime) * 100;
        return "%.2f%%".formatted(improvement);
    }

    private String generateConclusion(List<BenchmarkResult> results) {
        var totalPlatformTime = results.stream()
                .mapToLong(BenchmarkResult::platformThreadsMs)
                .sum();
        var totalVirtualTime = results.stream()
                .mapToLong(BenchmarkResult::virtualThreadsMs)
                .sum();

        var avgImprovement = ((double) (totalPlatformTime - totalVirtualTime) / totalPlatformTime) * 100;

        return "Virtual threads showed an average improvement of %.2f%% over platform threads".formatted(avgImprovement);
    }

    private void validateRequestCount(int requests) {
        if (requests <= 0) {
            throw new IllegalArgumentException("Requests must be positive");
        }
        if (requests > config.getMaxRequests()) {
            throw new IllegalArgumentException("Requests cannot exceed " + config.getMaxRequests());
        }
    }
}
