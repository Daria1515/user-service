# User Service

REST API сервис для управления пользователями с документацией Swagger и поддержкой H2 базы данных.

## Возможности

- ✅ CRUD операции для пользователей
- ✅ Swagger/OpenAPI документация
- ✅ Валидация данных
- ✅ H2 in-memory база данных
- ✅ Логирование
- ⏸️ HATEOAS (временно отключено из-за проблем совместимости)
- ⏸️ Kafka интеграция (временно отключена)

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

## Структура проекта

```
src/main/java/org/example/
├── config/
│   ├── KafkaConfig.java
│   └── OpenApiConfig.java
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
