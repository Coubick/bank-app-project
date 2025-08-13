package org.example.clients;

import org.example.DTO.AccountInfoDTO;
import org.example.user.AppUser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
public class AdminClient {
    private final RestTemplate restTemplate;
    private final String USERS_URL = "http://localhost:8080/api/v3/bank_system/users";
    private final String ACCOUNTS_URL = "http://localhost:8080/api/v3/bank_system/accounts";

    public AdminClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void post(AppUser appUser) {
        restTemplate.postForObject(USERS_URL, appUser, AppUser.class);
    }

    public List<AccountInfoDTO> fetchAllAccounts() {
        return restTemplate.exchange(
                ACCOUNTS_URL + "/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AccountInfoDTO>>() {}
        ).getBody();
    }

    public List<AppUser> fetchFilteredUsers(String gender, String color) {
        String url = UriComponentsBuilder.fromUriString(USERS_URL)
                .queryParam("gender", gender)
                .queryParam("color", color)
                .toUriString();

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AppUser>>() {}
        ).getBody();
    }

    public AppUser fetchUserInfo(String login) {
        return restTemplate.getForObject(USERS_URL + "/" + login, AppUser.class);
    }
}