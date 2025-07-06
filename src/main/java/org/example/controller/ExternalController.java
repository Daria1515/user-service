package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.service.ExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external")
@Tag(name = "External Services", description = "API для тестирования Circuit Breaker")
public class ExternalController {

    @Autowired
    private ExternalService externalService;

    @GetMapping("/call/{serviceName}")
    @Operation(summary = "Вызвать внешний сервис", description = "Демонстрирует работу Circuit Breaker")
    public ResponseEntity<String> callExternalService(
            @Parameter(description = "Имя внешнего сервиса", required = true) 
            @PathVariable String serviceName) {
        String result = externalService.callExternalApi(serviceName);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/unreliable")
    @Operation(summary = "Вызвать ненадежный сервис", description = "Демонстрирует Circuit Breaker с fallback")
    public ResponseEntity<String> callUnreliableService() {
        String result = externalService.callUnreliableService();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/health")
    @Operation(summary = "Проверка здоровья сервиса", description = "Возвращает статус сервиса")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("User Service is healthy");
    }
} 