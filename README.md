# Java 21 Virtual Threads Benchmark

A Spring Boot application that demonstrates the performance benefits of Java 21 Virtual Threads compared to traditional platform threads for I/O-bound operations.

## 🚀 Technology Stack

- **Java 21** - Latest LTS with Virtual Threads
- **Spring Boot 3.2** - Enterprise framework
- **Gradle** - Build tool
- **Lombok** - Reduced boilerplate code
- **HTTP Client** - Java's built-in HTTP client
- **SLF4J** - Logging facade

## 📋 Prerequisites

- Java 21 or later
- Gradle 7.6+ or compatible version
- Internet connection (for external HTTP calls)

## 🛠️ Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/johnnygomes84/java_virtual.git
   cd java_virtual
   ```

2. **Build the application**
   ```bash
   ./gradlew build
   ```

3. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

   The application will start on `http://localhost:8080`

## 🌟 API Endpoints

### 1. Single Benchmark
**Endpoint:** `GET /api/benchmark`

Runs a benchmark comparing virtual threads vs platform threads for the specified number of requests.

**Parameters:**
- `requests` (optional, default: 100) - Number of concurrent requests to make (1-1000)

**Example Request:**
```bash
curl "http://localhost:8888/api/benchmark?requests=100"
```

**Example Response:**
```json
{
  "requests": 100,
  "platformThreadsMs": 10560,
  "virtualThreadsMs": 1240,
  "performanceImprovement": "88.26%"
}
```

### 2. Summary Benchmark
**Endpoint:** `GET /api/benchmark/summary`

Runs multiple benchmarks with different request counts (10, 50, 100, 200) and provides aggregated results with a performance conclusion.

**Example Request:**
```bash
curl "http://localhost:8888/api/benchmark/summary?requestCounts=5,10,15"
```

**Example Response:**
```json
{
  "benchmarks": [
    {
      "requests": 5,
      "platformThreadsMs": 3267,
      "virtualThreadsMs": 2287,
      "performanceImprovement": "30.00%"
    },
    {
      "requests": 10,
      "platformThreadsMs": 3547,
      "virtualThreadsMs": 3797,
      "performanceImprovement": "-7.05%"
    },
    {
      "requests": 15,
      "platformThreadsMs": 5575,
      "virtualThreadsMs": 5114,
      "performanceImprovement": "8.27%"
    }
  ],
  "conclusion": "Virtual threads showed an average improvement of 9.61% over platform threads"
}
```

## ⚙️ Configuration

Update `src/main/resources/application.yml` to customize the behavior:

```yaml
app:
  benchmark:
    target-url: "https://httpbin.org/delay/1"  # Target endpoint for benchmarking
    platform-thread-pool-size: 10              # Size of platform thread pool
    timeout-seconds: 10                        # HTTP request timeout
    max-requests: 1000                         # Maximum allowed requests

logging:
  level:
    com.example.virtualthreadsbenchmark: INFO  # Application logging level
```

## 🚀 Quick Start

```bash
# Start the application
./gradlew bootRun

# Test with 50 requests
curl "http://localhost:8080/api/benchmark?requests=50"

# Get comprehensive summary
curl "http://localhost:8080/api/benchmark/summary"
```
