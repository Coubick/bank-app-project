package org.example.user;

import lombok.Data;

@Data
public class AppUser {
    private String login;
    private String name;
    private String gender;
    private String hairColor;
    private int age;
}
