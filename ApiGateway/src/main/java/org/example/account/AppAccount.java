package org.example.account;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AppAccount {
    private int userDefinedId;
    private BigDecimal balance = BigDecimal.valueOf(0);
    private String ownerLogin;
    private Integer id;
}
