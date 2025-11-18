package com.example.java_virtual.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.benchmark")
public class BenchmarkConfig {
    private String targetUrl = "https://httpbin.org/delay/1";
    private int platformThreadPoolSize = 10;
    private int timeoutSeconds = 10;
}
