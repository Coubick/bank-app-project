package org.example.user_dao;

import lombok.Data;
import org.example.enums.Colors;
import org.example.enums.Gender;

import jakarta.persistence.*;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")

    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "hair_color")
    private Colors hairColor;

    @Column(name = "age")
    private int age;

    @ManyToMany
    @JoinTable(name = "friendships",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private List<User> friends;

    public User() {
    }

    public User(String login, String name, Gender gender, Colors hairColor, int age) {
        this.login = login;
        this.name = name;
        this.gender = gender;
        this.hairColor = hairColor;
        this.age = age;
    }
}
