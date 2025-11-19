package com.example.java_virtual.service;

import com.example.java_virtual.config.BenchmarkConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class HttpRequestService {

    private final BenchmarkConfig config;
    private final HttpClient httpClient;

    public void performHttpRequest(int taskId) {
        try {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getTargetUrl()))
                    .GET()
                    .timeout(Duration.ofSeconds(config.getTimeoutSeconds()))
                    .build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.warn("Task {} received non-200 status: {}", taskId, response.statusCode());
            }

            log.debug("Task {} completed successfully", taskId);

        } catch (Exception e) {
            log.error("Task {} failed: {}", taskId, e.getMessage());
            throw new RuntimeException("HTTP request failed for task " + taskId, e);
        }
    }
}
