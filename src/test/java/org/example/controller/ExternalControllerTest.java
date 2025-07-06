package org.example.controller;

import org.example.service.ExternalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExternalController.class)
class ExternalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExternalService externalService;

    @Test
    void testCallExternalService_Success() throws Exception {
        // Arrange
        when(externalService.callExternalApi(anyString()))
                .thenReturn("External service test-service is healthy");

        // Act & Assert
        mockMvc.perform(get("/api/external/call/test-service"))
                .andExpect(status().isOk())
                .andExpect(content().string("External service test-service is healthy"));
    }

    @Test
    void testCallExternalService_Fallback() throws Exception {
        // Arrange
        when(externalService.callExternalApi(anyString()))
                .thenReturn("External service test-service is currently unavailable. Using fallback response.");

        // Act & Assert
        mockMvc.perform(get("/api/external/call/test-service"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("fallback")));
    }

    @Test
    void testCallUnreliableService_Success() throws Exception {
        // Arrange
        when(externalService.callUnreliableService())
                .thenReturn("Service response: 0.8");

        // Act & Assert
        mockMvc.perform(get("/api/external/unreliable"))
                .andExpect(status().isOk())
                .andExpect(content().string("Service response: 0.8"));
    }

    @Test
    void testCallUnreliableService_Fallback() throws Exception {
        // Arrange
        when(externalService.callUnreliableService())
                .thenReturn("Fallback response due to: Service temporarily unavailable");

        // Act & Assert
        mockMvc.perform(get("/api/external/unreliable"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Fallback response")));
    }

    @Test
    void testHealth() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/external/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("User Service is healthy"));
    }
} 