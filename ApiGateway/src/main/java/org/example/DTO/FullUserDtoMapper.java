package org.example.DTO;

import org.example.user.AppUser;
import org.example.user.GatewayUser;
import org.springframework.stereotype.Component;

@Component
public class FullUserDtoMapper {

    public FullUserDTO toFullUserDTO(AppUser appUser, GatewayUser gatewayUser) {
        if (appUser == null || gatewayUser == null) {
            return null;
        }

        FullUserDTO fullUserDTO = new FullUserDTO();
        fullUserDTO.setLogin(appUser.getLogin());
        fullUserDTO.setUserName(appUser.getName());
        fullUserDTO.setGender(appUser.getGender());
        fullUserDTO.setAge(appUser.getAge());
        fullUserDTO.setHairColor(appUser.getHairColor());
        fullUserDTO.setRole(gatewayUser.getRole());
        fullUserDTO.setPassword(gatewayUser.getPassword());
        return fullUserDTO;
    }
}