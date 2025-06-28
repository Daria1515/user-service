# User Service и Notification Service - Локальный запуск

Этот проект содержит два микросервиса для тестирования интеграции через Kafka.

## Архитектура

- **user-service** (порт 8090) - управление пользователями, отправка событий в Kafka
- **notification-service** (порт 8081) - получение событий из Kafka, отправка email

## Быстрый старт (с H2 базой данных)

### 1. Запуск user-service

```bash
cd user-service/user-service
mvn spring-boot:run -Dspring.profiles.active=h2
```

### 2. Запуск notification-service

```bash
cd notification-service
mvn spring-boot:run
```

## Тестирование

### Создание пользователя
```bash
curl -X POST http://localhost:8090/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Иван Иванов",
    "email": "ivan@example.com"
  }'
```

### Получение пользователя
```bash
curl http://localhost:8090/api/users/1
```

### Удаление пользователя
```bash
curl -X DELETE http://localhost:8090/api/users/1
```

### Просмотр H2 консоли
Откройте http://localhost:8090/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## Логи

- user-service: `user-service/user-service/logs/application.log`
- notification-service: логи в консоли

## Альтернативный вариант с PostgreSQL

Если хотите использовать PostgreSQL:

1. Установите PostgreSQL: https://www.postgresql.org/download/windows/
2. Создайте базу данных: `user_service_db`
3. Запустите user-service без профиля: `mvn spring-boot:run`

## Структура событий Kafka

События отправляются в топик `user-events`:

```json
{
  "email": "user@example.com",
  "operation": "CREATE" // или "DELETE"
}
```

## Настройка email

Для реальной отправки email настройте в `notification-service/src/main/resources/application.yml`:

```yaml
spring:
  mail:
    username: your-email@gmail.com
    password: your-app-password
```

Для тестирования используется заглушка. 