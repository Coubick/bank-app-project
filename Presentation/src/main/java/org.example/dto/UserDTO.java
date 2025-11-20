package org.example.dto;

import lombok.Data;
import org.example.enums.Colors;
import org.example.enums.Gender;

@Data
public class UserDTO {
    private String name;
    private String login;
    private int age;
    private Gender gender;
    private Colors hairColor;

    public UserDTO() {
    }
}
