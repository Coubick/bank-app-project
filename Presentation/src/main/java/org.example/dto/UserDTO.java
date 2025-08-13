package org.example.dto;

import org.example.enums.Colors;
import org.example.enums.Gender;

public class UserDTO {
    private String userName;
    private String login;
    private int age;
    private Gender gender;
    private Colors hairColor;
    public UserDTO() {}

    public UserDTO(String userName, String login, int age, Gender gender, Colors hairColor) {
        this.userName = userName;
        this.login = login;
        this.age = age;
        this.gender = gender;
        this.hairColor = hairColor;
    }

    public String getLogin() { return login; }

    public void setLogin(String login) { this.login = login; }

    public int getAge() { return age; }

    public void setAge(int age) { this.age = age; }

    public Gender getGender() { return gender; }

    public void setGender(Gender gender) { this.gender = gender; }

    public Colors getHairColor() { return hairColor; }

    public void setHairColor(Colors hairColor) { this.hairColor = hairColor; }

    public String getUserName() { return userName; }

    public void setUserName(String name) { this.userName=name; }
}
