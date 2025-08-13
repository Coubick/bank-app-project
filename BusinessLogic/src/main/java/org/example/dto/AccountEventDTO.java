package org.example.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountEventDTO {
    private int userDefinedId;
    private BigDecimal balance;
    private String ownerLogin;
    private String eventType;
    private LocalDateTime eventTime;

    public AccountEventDTO(int userDefinedId, BigDecimal balance, String ownerLogin, String eventType, LocalDateTime eventTime) {
        this.userDefinedId = userDefinedId;
        this.balance = balance;
        this.ownerLogin = ownerLogin;
        this.eventType = eventType;
        this.eventTime = eventTime;
    }
}