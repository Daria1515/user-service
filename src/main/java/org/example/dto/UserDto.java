package org.example.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Schema(description = "DTO для пользователя")
public class UserDto {

    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;
    
    @Schema(description = "Имя пользователя", example = "Иван Иванов", required = true)
    @NotBlank(message = "Имя не должно быть пустым")
    private String name;
    
    @Schema(description = "Email пользователя", example = "ivan@example.com", required = true)
    @Email(message = "Некорректный email")
    private String email;
    
    @Schema(description = "Возраст пользователя", example = "25", minimum = "0", maximum = "150")
    @Min(value = 0, message = "Возраст не может быть отрицательным")
    @Max(value = 150, message = "Возраст не может быть больше 150")
    private Integer age;
    
    @Schema(description = "Дата и время создания пользователя", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    public UserDto() {}

    public UserDto(Long id, String name, String email, Integer age, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
