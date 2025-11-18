package com.example.java_virtual.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThreadBenchmarkService {

    private final HttpRequestService httpRequestService;

    public long runWithPlatformThreads(int requests, int threadPoolSize) {
        log.info("Running with platform threads - requests: {}, pool size: {}, initial threads: {}",
                requests, threadPoolSize, Thread.activeCount());

        var executor = Executors.newFixedThreadPool(threadPoolSize);

        try {
            return executeBenchmark(requests, executor, "platform");
        } finally {
            executor.shutdown();
        }
    }

    public long runWithVirtualThreads(int requests) {
        log.info("Running with virtual threads - requests: {}, initial threads: {}",
                requests, Thread.activeCount());

        var executor = Executors.newVirtualThreadPerTaskExecutor();

        try {
            return executeBenchmark(requests, executor, "virtual");
        } finally {
            executor.shutdown();
        }
    }

    private long executeBenchmark(int requests, ExecutorService executor, String threadType) {
        var startTime = Instant.now();

        List<Future<?>> futures = IntStream.range(0, requests)
                .mapToObj(taskId -> executor.submit(() -> httpRequestService.performHttpRequest(taskId)))
                .collect(java.util.stream.Collectors.toList());

        waitForAllFutures(futures);

        var duration = Duration.between(startTime, Instant.now()).toMillis();
        log.info("{} threads completed - final threads: {}, time: {}ms",
                threadType, Thread.activeCount(), duration);

        return duration;
    }

    private void waitForAllFutures(List<Future<?>> futures) {
        futures.forEach(future -> {
            try {
                future.get();
            } catch (Exception e) {
                throw new RuntimeException("Task execution failed", e);
            }
        });
    }
}
