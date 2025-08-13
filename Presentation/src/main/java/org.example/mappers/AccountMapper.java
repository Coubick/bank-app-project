package org.example.mappers;

import org.example.account_dao.Account;
import org.example.dto.AccountDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountMapper {

    public Account toEntity(AccountDTO dto) {
        Account account = new Account();
        account.setUserDefinedId(dto.getUserDefinedId());
        account.setOwnerLogin(dto.getOwnerLogin());
        account.setBalance(dto.getBalance());
        return account;
    }

    public AccountDTO toDto(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setUserDefinedId(account.getUserDefinedId());
        dto.setBalance(account.getBalance());
        dto.setOwnerLogin(account.getOwnerLogin());
        return dto;
    }

    public List<AccountDTO> toDtoList(List<Account> accounts) {
        return accounts.stream().map(this::toDto).collect(Collectors.toList());
    }
}
