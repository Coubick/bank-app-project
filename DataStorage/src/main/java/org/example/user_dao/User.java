package org.example.user_dao;

import org.example.enums.AddFindDeleteResult;
import org.example.enums.Colors;
import org.example.enums.Gender;

import jakarta.persistence.*;

import java.util.List;

/**
 * Класс пользователя
 * Содержит геттеры для получения значений полей
 */
@Entity
@Table(name = "users")
public class User {
    @Column(unique = true, nullable = false)
    private String login;
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")

    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "hair_color")
    private Colors hairColor;

    private int age;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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
        this.age = age;
        this.gender = gender;
        this.hairColor = hairColor;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public Colors getHairColor() {
        return hairColor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setHairColor(Colors color) {
        this.hairColor = color;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<User> getFriends() {
        return friends;
    }

//    public AddFindDeleteResult addFriend(User user) {
//        if (!friends.contains(user)) {
//            friends.add(user);
//            return AddFindDeleteResult.Success;
//        }
//        return AddFindDeleteResult.AlreadyExists;
//    }
//
//    public AddFindDeleteResult deleteFriend(User user) {
//        if (friends.contains(user)) {
//            friends.remove(user);
//            return AddFindDeleteResult.Success;
//        }
//
//        return AddFindDeleteResult.UserNotFound;
//    }
}
