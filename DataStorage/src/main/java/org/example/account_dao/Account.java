package org.example.account_dao;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;


@Entity
@Table(name = "accounts")

@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_defined_id", unique = true, nullable = false)
    private int userDefinedId;

    private BigDecimal balance = BigDecimal.valueOf(0);

    @Column(name = "owner_id")
    private int ownerId;

    @Column(name = "owner_login")
    private String ownerLogin;

    public Account() {}

    public Account(int userDefinedId, String login){
        this.userDefinedId = userDefinedId;
        ownerLogin = login;
    }
}
