# User Service

REST API сервис для управления пользователями с документацией Swagger, поддержкой HATEOAS.

## Возможности

- CRUD операции для пользователей
- Swagger/OpenAPI документация
- HATEOAS (Hypermedia as the Engine of Application State)
- Валидация данных
- H2 in-memory база данных
- Логирование
- Kafka интеграция

## Запуск приложения

### Требования
- Java 17+
- Maven 3.6+

### Способы запуска

1. **Через Maven:**
   ```bash
   mvn spring-boot:run
   ```

2. **Через batch файл (Windows):**
   ```bash
   run.bat
   ```

3. **Сборка и запуск JAR:**
   ```bash
   mvn clean package
   java -jar target/user-service-1.0-SNAPSHOT.jar
   ```

## Доступные эндпоинты

После запуска приложение будет доступно на порту **8090**.

### API Endpoints

- **GET** `/api/users` - Получить всех пользователей
- **GET** `/api/users/{id}` - Получить пользователя по ID
- **POST** `/api/users` - Создать нового пользователя
- **PUT** `/api/users/{id}` - Обновить пользователя
- **DELETE** `/api/users/{id}` - Удалить пользователя

### Swagger UI

- **Swagger UI:** http://localhost:8090/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8090/api-docs

### H2 Console

- **H2 Console:** http://localhost:8090/h2-console
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** `password`

## Тестирование API

### Создание пользователя

```bash
curl -X POST http://localhost:8090/api/users \
  -H "Content-Type: application/json" \
  -d @test-user.json
```

Или используя PowerShell:

```powershell
Invoke-RestMethod -Uri "http://localhost:8090/api/users" -Method POST -ContentType "application/json" -InFile "test-user.json"
```

### Получение всех пользователей

```bash
curl http://localhost:8090/api/users
```

### Получение пользователя по ID

```bash
curl http://localhost:8090/api/users/1
```

## HATEOAS (Hypermedia as the Engine of Application State)

API поддерживает HATEOAS, что означает, что каждый ответ содержит ссылки на связанные ресурсы.

### Пример ответа с HATEOAS

**GET /api/users/1**
```json
{
  "id": 1,
  "name": "Иван Иванов",
  "email": "ivan@example.com",
  "age": 25,
  "createdAt": "2024-01-15T10:30:00",
  "_links": {
    "self": {
      "href": "http://localhost:8090/api/users/1"
    },
    "users": {
      "href": "http://localhost:8090/api/users"
    },
    "update": {
      "href": "http://localhost:8090/api/users/1"
    },
    "delete": {
      "href": "http://localhost:8090/api/users/1"
    }
  }
}
```

### Навигация по API

Клиенты могут использовать ссылки в `_links` для навигации по API:

- **self** - ссылка на текущий ресурс
- **users** - ссылка на список всех пользователей
- **update** - ссылка для обновления пользователя
- **delete** - ссылка для удаления пользователя

### Запрос с поддержкой HATEOAS

```bash
curl -H "Accept: application/hal+json" http://localhost:8090/api/users/1
```

## Структура проекта

```
src/main/java/org/example/
├── config/
│   ├── KafkaConfig.java
│   ├── OpenApiConfig.java
│   └── HateoasConfig.java
├── controller/
│   └── UserController.java
├── dto/
│   └── UserDto.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   └── UserNotFoundException.java
├── mapper/
│   └── UserMapper.java
├── model/
│   └── User.java
├── repository/
│   └── UserRepository.java
├── service/
│   └── UserService.java
└── UserServiceApplication.java
```

## Конфигурация

Основные настройки находятся в `src/main/resources/application.properties`:

- **Порт:** 8090
- **База данных:** H2 in-memory
- **JPA:** create-drop (таблицы пересоздаются при каждом запуске)
- **Логирование:** DEBUG для пакета org.example
- **HATEOAS:** HAL JSON как формат по умолчанию

## Устранение неполадок

### Проблема с портом 8090
Если порт занят, завершите процессы Java:
```bash
taskkill /f /im java.exe
```

### Проблемы с кодировкой в PowerShell
Используйте файл JSON для POST-запросов вместо командной строки.

### Проблемы с зависимостями
Если возникают ошибки компиляции, выполните:
```bash
mvn clean compile
```

### Swagger UI не загружается
Проверьте, что приложение запущено и доступно по адресу http://localhost:8090/swagger-ui.html

### Ошибки 500 при создании пользователя
- Проверьте логи в `logs/application.log`
- Убедитесь, что все поля заполнены корректно
- Используйте файл JSON для кириллицы

### Проблемы с HATEOAS
- Убедитесь, что в заголовке Accept указан `application/hal+json`
- Проверьте, что HATEOAS зависимости корректно подключены
- Запустите `compile.bat` для проверки компиляции
- Если есть ошибки, попробуйте `mvn clean install`

### Проверка работы HATEOAS
```bash
# Обычный запрос (без HATEOAS)
curl http://localhost:8090/api/users/1

# Запрос с HATEOAS
curl -H "Accept: application/hal+json" http://localhost:8090/api/users/1
```

В ответе с HATEOAS вы увидите секцию `_links` с ссылками на связанные ресурсы.

## Разработка

### Запуск тестов
```bash
mvn test
```

### Покрытие кода
```bash
mvn jacoco:report
```

Отчет о покрытии будет доступен в `target/site/jacoco/index.html`

## Использование Swagger UI

Swagger UI предоставляет интерактивную документацию API:

1. Откройте http://localhost:8090/swagger-ui.html
2. Выберите нужный endpoint
3. Нажмите "Try it out"
4. Заполните параметры и нажмите "Execute"
5. Изучите ответ сервера

Это самый удобный способ тестирования API!

## Преимущества HATEOAS

- 🧭 **Навигация** - клиенты могут открывать API без знания URL
- 🔗 **Связанность** - ресурсы связаны между собой
- 🔄 **Эволюция** - API может развиваться без ломания клиентов
- 📚 **Самоописание** - API сам описывает доступные действия