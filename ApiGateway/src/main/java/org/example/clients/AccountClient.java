package org.example.clients;

import org.example.DTO.AccountInfoDTO;
import org.example.account.AppAccount;
import org.example.user.AppUser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountClient {
    private final RestTemplate restTemplate;
    private final String BASE_URL = "http://localhost:8080/api/v3/bank_system/accounts";

    public AccountClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void post(AppAccount appAccount){
        restTemplate.postForObject(BASE_URL + "/create", appAccount, AppAccount.class);
    }

    public List<AccountInfoDTO> getAllAccounts() {
        return restTemplate.exchange(
                BASE_URL + "/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AccountInfoDTO>>() {
                }
        ).getBody();
    }

    public void depositAccount(BigDecimal amount, int id) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/{id}/deposit")
                .queryParam("amount", amount)
                .buildAndExpand(id)
                .toUriString();
        restTemplate.put(url, null);
    }

    public void withdrawAccount(BigDecimal amount, int id) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/{id}/withdraw")
                .queryParam("amount", amount)
                .buildAndExpand(id)
                .toUriString();

        restTemplate.put(url, null);
    }

    public void transfer(BigDecimal amount, int senderAccountId, int receiverAccountId) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/{senderId}/transfer/{receiverId}")
                .queryParam("amount", amount)
                .buildAndExpand(senderAccountId, receiverAccountId)
                .toUriString();

        restTemplate.put(url, null);
    }
}
