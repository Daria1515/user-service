# User Service - Микросервисное приложение

Spring Boot приложение для управления пользователями с поддержкой микросервисных паттернов.

## Архитектура

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  User Service   │    │Notification Svc │    │   PostgreSQL    │
│   (Port 8081)   │    │  (Port 8084)    │    │   (Port 5433)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │     Kafka       │
                    │   (Port 9092)   │
                    └─────────────────┘
```

## Сервисы

### User Service (Port 8081)
- CRUD операции для пользователей
- Отправка событий в Kafka при создании/удалении
- Swagger документация
- HATEOAS поддержка

### Notification Service (Port 8084)
- Получение событий из Kafka
- Отправка email уведомлений
- Swagger документация

## Возможности

- CRUD операции для пользователей
- Swagger/OpenAPI документация
- HATEOAS (Hypermedia as the Engine of Application State)
- Kafka интеграция (события CREATE/DELETE)
- Email уведомления с HTML шаблонами
- Валидация данных
- PostgreSQL база данных
- Логирование

## Настройка Email (Gmail)

### 1. Подготовка Gmail аккаунта

Для отправки настоящих писем необходимо настроить Gmail:

1. **Включите двухфакторную аутентификацию:**
   - Перейдите в https://myaccount.google.com/security
   - Включите "Двухэтапную аутентификацию"

2. **Создайте пароль приложения:**
   - Перейдите в https://myaccount.google.com/apppasswords
   - Выберите "Почта" и "Другое (пользовательское имя)"
   - Введите название: "Notification Service"
   - Скопируйте сгенерированный пароль (16 символов)

### 2. Настройка переменных окружения

```bash
# Windows
set EMAIL_USERNAME=your-email@gmail.com
set EMAIL_PASSWORD=your-16-char-app-password

# Linux/Mac
export EMAIL_USERNAME=your-email@gmail.com
export EMAIL_PASSWORD=your-16-char-app-password
```

### 3. Или настройка в application.properties

```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-16-char-app-password
```

## Запуск приложения

### 1. Запуск инфраструктуры (Kafka + PostgreSQL)

```bash
# Запуск Kafka и PostgreSQL через Docker Compose
docker-compose up -d kafka zookeeper postgres
```

### 2. Запуск User Service

```bash
# В папке user-service
mvn clean compile
mvn spring-boot:run -Dspring-boot.run.profiles=dev -Dserver.port=8081
```

### 3. Запуск Notification Service

```bash
# В папке notification-service
mvn clean compile
mvn spring-boot:run -Dspring-boot.run.profiles=docker -Dserver.port=8084
```

## Доступные сервисы

| Сервис | URL | Описание |
|--------|-----|----------|
| User Service | http://localhost:8081 | Основное приложение |
| Notification Service | http://localhost:8084 | Сервис уведомлений |
| Swagger UI (User) | http://localhost:8081/swagger-ui.html | API документация |
| Swagger UI (Notif) | http://localhost:8084/swagger-ui.html | API документация |
| H2 Console | http://localhost:8081/h2-console | База данных (dev) |
| Actuator (User) | http://localhost:8081/actuator | Мониторинг |
| Actuator (Notif) | http://localhost:8084/actuator | Мониторинг |

## API Endpoints

### User Service
- `GET /api/users` - Получить всех пользователей
- `GET /api/users/{id}` - Получить пользователя по ID
- `POST /api/users` - Создать пользователя
- `PUT /api/users/{id}` - Обновить пользователя
- `DELETE /api/users/{id}` - Удалить пользователя

### Notification Service
- `POST /api/email/send` - Отправить тестовое письмо
- `POST /api/email/welcome` - Отправить приветственное письмо
- `POST /api/email/goodbye` - Отправить прощальное письмо
- `GET /api/email/test` - Тест email сервиса

## Тестирование

### 1. Создание пользователя
1. Откройте http://localhost:8081/swagger-ui.html
2. Выберите сервер: http://localhost:8081
3. Выполните POST /api/users с данными пользователя
4. Проверьте, что приветственное письмо отправлено

### 2. Удаление пользователя
1. Выполните DELETE /api/users/{id}
2. Проверьте, что прощальное письмо отправлено

### 3. Тестирование email напрямую
1. Откройте http://localhost:8084/swagger-ui.html
2. Выполните POST /api/email/send для тестирования

## Конфигурация

### Профили
- `dev` - H2 база данных, локальная разработка
- `docker` - PostgreSQL, Kafka

### Порты
- User Service: 8081
- Notification Service: 8084
- PostgreSQL: 5433
- Kafka: 9092
- Zookeeper: 2181

## Требования

- Java 17+
- Maven 3.6+
- Docker (для Kafka и PostgreSQL)
- Gmail аккаунт (для email уведомлений)
