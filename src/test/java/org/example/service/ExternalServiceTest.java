package org.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private ExternalService externalService;

    @BeforeEach
    void setUp() {
        externalService = new ExternalService(restTemplate);
    }

    @Test
    void callExternalApi_ShouldReturnResponse_WhenServiceIsAvailable() {
        // Given
        String serviceName = "test-service";
        String expectedResponse = "External service response";
        when(restTemplate.getForObject(eq("http://" + serviceName + "/api/status"), eq(String.class)))
                .thenReturn(expectedResponse);

        // When
        String result = externalService.callExternalApi(serviceName);

        // Then
        assertEquals(expectedResponse, result);
    }

    @Test
    void callExternalApi_ShouldReturnFallback_WhenServiceIsUnavailable() {
        // Given
        String serviceName = "test-service";
        when(restTemplate.getForObject(eq("http://" + serviceName + "/api/status"), eq(String.class)))
                .thenThrow(new RuntimeException("Connection failed"));

        // When
        String result = externalService.callExternalApi(serviceName);

        // Then
        assertEquals("External service test-service is currently unavailable. Using fallback response.", result);
    }

    @Test
    void callUnreliableService_ShouldReturnResponse_WhenServiceWorks() {
        // When
        String result = externalService.callUnreliableService();

        // Then
        assertTrue(result.startsWith("Service response: ") || result.startsWith("Fallback response due to: "));
    }

    @Test
    void callUnreliableService_ShouldReturnFallback_WhenServiceFails() {
        // When
        String result = externalService.callUnreliableService();

        // Then
        assertTrue(result.startsWith("Service response: ") || result.startsWith("Fallback response due to: "));
    }
} 