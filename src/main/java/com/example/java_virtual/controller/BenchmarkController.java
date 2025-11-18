package com.example.java_virtual.controller;

import com.example.java_virtual.dto.BenchmarkResponse;
import com.example.java_virtual.dto.BenchmarkSummaryResponse;
import com.example.java_virtual.service.BenchmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/benchmark")
@RequiredArgsConstructor
public class BenchmarkController {

    private final BenchmarkService benchmarkService;

    @GetMapping
    public ResponseEntity<BenchmarkResponse> runBenchmark(
            @RequestParam(defaultValue = "100") int requests) {

        var result = benchmarkService.runBenchmark(requests);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/summary")
    public ResponseEntity<BenchmarkSummaryResponse> runSummaryBenchmark() {

        var result = benchmarkService.runSummaryBenchmark();

        return ResponseEntity.ok(result);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.internalServerError().body("An unexpected error occurred");
    }
}
