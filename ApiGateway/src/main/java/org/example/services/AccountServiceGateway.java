package org.example.services;

import org.example.clients.AccountClient;
import org.example.DTO.AccountInfoDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AccountServiceGateway {
    private final AccountClient accountClient;

    public AccountServiceGateway(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    public List<AccountInfoDTO> getMyAccountById(int id, Authentication authentication) {
        List<AccountInfoDTO> accounts = accountClient.getAllAccounts().stream().toList();

        return accounts.stream()
                .filter(account -> account.getOwnerLogin().equals(authentication.getName())
                        && account.getUserDefinedId() == id)
                .collect(Collectors.toList());
    }

    public void depositAccount(int id, BigDecimal amount, Authentication authentication) {
        List<AccountInfoDTO> accounts = getMyAccountById(id, authentication);
        if (accounts.isEmpty()) {
            throw new NoSuchElementException("Account not found");
        }

        accountClient.depositAccount(amount, id);
    }

    public void withdrawAccount(int id, Authentication authentication, BigDecimal amount) {
        List<AccountInfoDTO> accounts = getMyAccountById(id, authentication);
        if (accounts.isEmpty()) {
            throw new NoSuchElementException("Account not found");
        }

        accountClient.withdrawAccount(amount, id);
    }

    public void transfer(int senderAccountId,
                         int receiverAccountId,
                         BigDecimal amount,
                         Authentication authentication) {
        List<AccountInfoDTO> accounts = getMyAccountById(senderAccountId, authentication);
        if (accounts.isEmpty()) {
            throw new NoSuchElementException("Sender account not found");
        }

        accountClient.transfer(amount, senderAccountId, receiverAccountId);
    }

    public List<AccountInfoDTO> getMyAccounts(Authentication authentication) {
        List<AccountInfoDTO> accounts = accountClient.getAllAccounts().stream().toList();

        return accounts.stream()
                .filter(account -> account.getOwnerLogin().equals(authentication.getName()))
                .collect(Collectors.toList());
    }
}