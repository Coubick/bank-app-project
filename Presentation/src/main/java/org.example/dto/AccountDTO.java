package org.example.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDTO {
    private int userDefinedId;
    private BigDecimal balance;
    private int ownerId;
    private String ownerLogin;

    public AccountDTO() {}

    public AccountDTO(int userDefinedId, BigDecimal balance, int ownerId, String ownerLogin) {
        this.userDefinedId = userDefinedId;
        this.balance = balance;
        this.ownerId = ownerId;
        this.ownerLogin = ownerLogin;
    }
}
