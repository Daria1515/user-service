# Микросервисная архитектура с паттернами

Spring Boot приложение для управления пользователями с поддержкой микросервисных паттернов.

## Архитектура

### Сервисы:
- **Eureka Server** (порт 8761) - Service Discovery
- **Config Server** (порт 8888) - External Configuration
- **API Gateway** (порт 8080) - Gateway API
- **User Service** (порт 8081) - Основной сервис пользователей
- **Notification Service** (порт 8082) - Сервис уведомлений

### Инфраструктура:
- **PostgreSQL** (порт 5433) - База данных
- **Kafka** (порт 9092) - Message Broker

## Запуск

### Вариант 1: Docker Compose (рекомендуется)

```bash
# Сборка и запуск всех сервисов
docker-compose up --build

# Запуск в фоновом режиме
docker-compose up -d --build
```

### Вариант 2: Локальный запуск

1. **Запуск инфраструктуры:**
```bash
# PostgreSQL и Kafka
docker-compose up postgres zookeeper kafka -d
```

2. **Запуск сервисов по порядку:**
```bash
# 1. Eureka Server
cd eureka-server
mvn spring-boot:run

# 2. Config Server
cd ../config-server
mvn spring-boot:run

# 3. User Service
cd ../user-service
mvn spring-boot:run

# 4. Notification Service
cd ../notification-service
mvn spring-boot:run

# 5. API Gateway
cd ../api-gateway
mvn spring-boot:run
```

## 📋 Реализованные паттерны

### 1. **Service Discovery (Eureka)**
- Автоматическая регистрация сервисов
- Обнаружение сервисов по имени
- Health checks

### 2. **External Configuration (Config Server)**
- Централизованная конфигурация
- Локальные конфигурационные файлы
- Поддержка профилей

### 3. **Circuit Breaker (Resilience4j)**
- Защита от каскадных сбоев
- Fallback методы
- Автоматическое восстановление

### 4. **Gateway API (Spring Cloud Gateway)**
- Единая точка входа
- Маршрутизация запросов
- Circuit breaker на уровне Gateway
- Fallback обработка

## Endpoints

### API Gateway (порт 8080):
- `http://localhost:8080/api/users/**` → User Service
- `http://localhost:8080/api/notifications/**` → Notification Service
- `http://localhost:8080/api/external/**` → External Service

### Eureka Dashboard:
- `http://localhost:8761` - Регистр сервисов

### Config Server:
- `http://localhost:8888/user-service/default` - Конфигурация User Service
- `http://localhost:8888/notification-service/default` - Конфигурация Notification Service

### Actuator Endpoints:
- `http://localhost:8080/actuator/health` - Health check Gateway
- `http://localhost:8081/actuator/health` - Health check User Service
- `http://localhost:8082/actuator/health` - Health check Notification Service

## Тестирование

### Проверка Service Discovery:
```bash
curl http://localhost:8761/eureka/apps
```

### Проверка Circuit Breaker:
```bash
# Вызов ненадежного сервиса
curl http://localhost:8080/api/external/unreliable
```

### Проверка Gateway:
```bash
# Через Gateway
curl http://localhost:8080/api/users/1

# Прямой вызов
curl http://localhost:8081/users/1
```

## Структура проекта

```
user-service/
├── api-gateway/          # API Gateway
├── config-server/        # Config Server
├── eureka-server/        # Eureka Server
├── src/                  # User Service
├── docker-compose.yml    # Docker Compose
└── README.md
```

## Конфигурация

### Circuit Breaker настройки:
- **external-api**: 50% failure rate, 10s wait time
- **unreliable-service**: 70% failure rate, 5s wait time
- **email-provider**: 50% failure rate, 10s wait time

### Gateway маршруты:
- `/api/users/**` → User Service
- `/api/notifications/**` → Notification Service
- `/api/external/**` → External Service (через User Service)

## Отладка

### Логи:
```bash
# Просмотр логов всех сервисов
docker-compose logs -f

# Логи конкретного сервиса
docker-compose logs -f user-service
```

### Мониторинг:
- Eureka Dashboard: `http://localhost:8761`
- Actuator endpoints для каждого сервиса
- Circuit breaker метрики в `/actuator/circuitbreakers`
