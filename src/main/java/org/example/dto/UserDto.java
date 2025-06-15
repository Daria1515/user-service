package org.example.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class UserDto {

    private Long id;
    @NotBlank(message = "Имя не должно быть пустым")
    private String name;
    @Email(message = "Некорректный email")
    private String email;
    @Min(value = 0, message = "Возраст не может быть отрицательным")
    private int age;
    private LocalDateTime createdAt;

    public UserDto() {}

    public UserDto(Long id, String name, String email, int age, LocalDateTime createdAt) {
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

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
