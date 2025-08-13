package org.example.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfoDTO {
    private int userDefinedId;
    private BigDecimal balance;
    private String ownerLogin;
}
