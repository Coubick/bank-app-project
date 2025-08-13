package org.example.dto;

import java.math.BigDecimal;

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

    public int getUserDefinedId() { return userDefinedId; }

    public void setUserDefinedId(int userDefinedId) { this.userDefinedId = userDefinedId;}

    public BigDecimal getBalance() { return balance; }

    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getOwnerLogin() { return ownerLogin; }

    public void setOwnerLogin(String ownerLogin) { this.ownerLogin = ownerLogin; }
}
