package org.example.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalService {

    private final RestTemplate restTemplate;

    public ExternalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "external-api", fallbackMethod = "callExternalApiFallback")
    public String callExternalApi(String serviceName) {
        // Симуляция вызова внешнего API
        String url = "http://" + serviceName + "/api/status";
        return restTemplate.getForObject(url, String.class);
    }

    public String callExternalApiFallback(String serviceName, Exception e) {
        return "External service " + serviceName + " is currently unavailable. Using fallback response. Error: " + e.getMessage();
    }

    @CircuitBreaker(name = "unreliable-service", fallbackMethod = "callUnreliableServiceFallback")
    public String callUnreliableService() {
        // Симуляция ненадежного сервиса
        double random = Math.random();
        if (random < 0.7) {
            throw new RuntimeException("Service temporarily unavailable");
        }
        return "Service response: " + random;
    }

    public String callUnreliableServiceFallback(Exception e) {
        return "Fallback response due to: " + e.getMessage();
    }
} 