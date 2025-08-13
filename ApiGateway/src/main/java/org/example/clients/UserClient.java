package org.example.clients;

import org.example.DTO.AccountInfoDTO;
import org.example.user.AppUser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class UserClient {
    private final RestTemplate restTemplate;
    private final static String APP_URL = "http://localhost:8080/api/v3/bank_system/users";
    private final String ACCOUNTS_URL = "http://localhost:8080/api/v3/bank_system/accounts";

    public UserClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AppUser getAppUserInfo(String login){
        return restTemplate.getForObject(APP_URL+"/"+login, AppUser.class);
    }

    public void addFriend(Authentication authentication, String friendLogin){
        restTemplate.put(APP_URL + "/" + authentication.getName() + "/friends/" + friendLogin, null);
    }

    public void removeFriend(Authentication authentication, String friendLogin){
        restTemplate.delete(APP_URL + "/" + authentication.getName() + "/friends/" + friendLogin);
    }

    public List<AppUser> getAppUsers(Authentication authentication){
        return restTemplate.exchange(
                APP_URL + "/" + authentication.getName() + "/friends",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AppUser>>() {}
        ).getBody();
    }

    public List<AccountInfoDTO> getAccounts(){
        return restTemplate.exchange(
                ACCOUNTS_URL + "/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AccountInfoDTO>>() {}
        ).getBody();
    }
}
