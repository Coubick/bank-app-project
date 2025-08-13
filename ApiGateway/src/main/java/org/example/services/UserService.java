package org.example.services;

import org.example.DTO.AccountInfoDTO;
import org.example.DTO.FriendsWithAccountsDTO;
import org.example.DTO.FullUserDTO;
import org.example.DTO.FullUserDtoMapper;
import org.example.clients.UserClient;
import org.example.user.AppUser;
import org.example.user.GatewayUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {
    private final FullUserDtoMapper fullUserDtoMapper;
    private final UserClient userClient;
    private final GatewayUserService gatewayUserService;

    private HttpStatus getErrorStatusCode(String message) {
        if (message.contains("404")) {
            return HttpStatus.NOT_FOUND;
        }
        else if (message.contains("409")) {
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public UserService(GatewayUserService gatewayUserService, FullUserDtoMapper fullUserMapper, UserClient userClient) {
        this.gatewayUserService = gatewayUserService;
        this.fullUserDtoMapper = fullUserMapper;
        this.userClient = userClient;
    }

    public FullUserDTO getUserInfo(String login){
        AppUser appUser = userClient.getAppUserInfo(login);
        GatewayUser gatewayUser = gatewayUserService.findByLogin(login);
        return fullUserDtoMapper.toFullUserDTO(appUser, gatewayUser);
    }

    public HttpStatus addFriend(Authentication authentication, String friendLogin) {
        try {
            userClient.addFriend(authentication, friendLogin);
        } catch (Exception e) {
            String err = e.getMessage();
            return getErrorStatusCode(err);
        }

        return HttpStatus.OK;
    }

    public HttpStatus removeFriend(Authentication authentication, String friendLogin){
        try {
            userClient.removeFriend(authentication, friendLogin);
        }
        catch (Exception e) {
            String err = e.getMessage();
            return getErrorStatusCode(err);
        }

        return HttpStatus.OK;
    }

    public List<FriendsWithAccountsDTO> getFriendsWithAccounts(Authentication authentication) {
        List<AppUser> friends =  userClient.getAppUsers(authentication);

        if (friends == null) return Collections.emptyList();

        return friends.stream()
                .map(friend -> {
                    List<AccountInfoDTO> accounts = userClient.getAccounts();
                    List<Integer> friendAccountIds = accounts.stream()
                            .filter(acc -> acc.getOwnerLogin().equals(friend.getLogin()))
                            .map(AccountInfoDTO::getUserDefinedId)
                            .collect(Collectors.toList());

                    return new FriendsWithAccountsDTO(friend.getLogin(), friendAccountIds);
                })
                .collect(Collectors.toList());
    }
}
