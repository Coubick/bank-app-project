package org.example.dto;

import lombok.Data;
import org.example.enums.Colors;
import org.example.enums.Gender;

import java.time.LocalDateTime;

@Data
public class UserEventDTO {
    private String login;
    private String name;
    private int age;
    private Gender gender;
    private Colors hairColor;
    private String eventType;
    private LocalDateTime eventDate;

    public UserEventDTO(String login, String name, int age, Gender gender, Colors hairColor, String eventType, LocalDateTime eventDate) {
        this.login = login;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.hairColor = hairColor;
        this.eventType = eventType;
        this.eventDate = eventDate;
    }
}