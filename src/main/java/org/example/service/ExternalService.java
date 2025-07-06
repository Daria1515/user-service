package org.example.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalService {

    private final RestTemplate restTemplate;

    public ExternalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String callExternalApi(String serviceName) {
        try {
            // Симуляция вызова внешнего API
            String url = "http://" + serviceName + "/api/status";
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            // Fallback метод
            return "External service " + serviceName + " is currently unavailable. Using fallback response.";
        }
    }

    public String callUnreliableService() {
        try {
            // Симуляция ненадежного сервиса
            double random = Math.random();
            if (random < 0.7) {
                throw new RuntimeException("Service temporarily unavailable");
            }
            return "Service response: " + random;
        } catch (Exception e) {
            return "Fallback response due to: " + e.getMessage();
        }
    }
} 