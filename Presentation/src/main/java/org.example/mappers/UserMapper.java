package org.example.mappers;

import org.example.dto.UserDTO;
import org.example.user_dao.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setLogin(dto.getLogin());
        user.setName(dto.getName());
        user.setGender(dto.getGender());
        user.setHairColor(dto.getHairColor());
        user.setAge(dto.getAge());
        return user;
    }

    public UserDTO toDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setName(user.getName());
        dto.setLogin(user.getLogin());
        dto.setAge(user.getAge());
        dto.setGender(user.getGender());
        dto.setHairColor(user.getHairColor());
        return dto;
    }

    public List<UserDTO> toDtoList(List<User> users) {
        return users.stream().map(this::toDto).toList();
    }
}
