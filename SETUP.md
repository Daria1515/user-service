# Быстрый старт для разработчика

## 📋 Требования

- Docker Desktop (или Docker Engine)
- Git
- Минимум 4 ГБ свободной RAM
- Порты 8080, 8081, 8082, 8761, 8888, 5433, 9092 должны быть свободны

## Быстрый запуск

### 1. Клонируйте репозиторий
```bash
git clone <repository-url>
cd user-service
```

### 2. Настройте email (опционально)
Отредактируйте `docker-compose.yml`:
```yaml
notification-service:
  environment:
    EMAIL_USERNAME: ваш-email@gmail.com
    EMAIL_PASSWORD: ваш-пароль-приложения
```

### 3. Запустите все сервисы
```bash
docker-compose up --build
```

### 4. Проверьте работу
- Eureka Dashboard: http://localhost:8761
- API Gateway: http://localhost:8080
- User Service: http://localhost:8081
- Notification Service: http://localhost:8082

## Тестирование

### Создание пользователя через Gateway
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Тест","email":"test@example.com"}'
```

### Проверка Service Discovery
```bash
curl http://localhost:8761/eureka/apps
```

## Устранение проблем

### Проблема: Порты заняты
**Решение:** Измените порты в `docker-compose.yml`:
```yaml
ports:
  - "8081:8080"  # Внешний:Внутренний
```

### Проблема: Недостаточно памяти
**Решение:** Запускайте по частям:
```bash
# Сначала инфраструктура
docker-compose up postgres kafka eureka-server config-server

# Потом сервисы
docker-compose up user-service notification-service api-gateway
```

### Проблема: Email не отправляется
**Решение:** Проверьте настройки Gmail:
1. Включите двухфакторную аутентификацию
2. Создайте пароль приложения
3. Обновите EMAIL_USERNAME и EMAIL_PASSWORD

## Мониторинг

### Логи
```bash
# Все сервисы
docker-compose logs -f

# Конкретный сервис
docker-compose logs -f user-service
```

### Health checks
```bash
curl http://localhost:8080/actuator/health  # Gateway
curl http://localhost:8081/actuator/health  # User Service
curl http://localhost:8082/actuator/health  # Notification Service
```

## Остановка

```bash
# Остановить все сервисы
docker-compose down

# Остановить и удалить данные
docker-compose down -v
```

## Поддержка

При возникновении проблем:
1. Проверьте логи: `docker-compose logs -f`
2. Проверьте статус контейнеров: `docker-compose ps`
3. Проверьте использование ресурсов: `docker stats` 