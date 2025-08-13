package org.example.DTO;

import lombok.Data;

@Data
public class FullUserDTO {
    private String login;
    private String password;
    private String role;
    private String name;
    private String gender;
    private String hairColor;
    private int age;
}
