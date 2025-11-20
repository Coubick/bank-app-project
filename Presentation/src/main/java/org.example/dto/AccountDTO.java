package org.example.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDTO {
    private int userDefinedId;
    private BigDecimal balance;
    private String ownerLogin;

    public AccountDTO() {}

    public AccountDTO(int userDefinedId, BigDecimal balance, String ownerLogin) {
        this.userDefinedId = userDefinedId;
        this.balance = balance;
        this.ownerLogin = ownerLogin;
    }
}
