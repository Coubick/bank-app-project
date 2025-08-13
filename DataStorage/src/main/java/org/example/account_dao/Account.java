package org.example.account_dao;

import jakarta.persistence.*;
import java.math.BigDecimal;


/**
 * Класс счёта
 * Содержит геттеры для полей счета
 */
@Entity
@Table(name = "accounts")

public class Account {
    @Column(name = "user_defined_id", unique = true, nullable = false)
    private int userDefinedId;

    private BigDecimal balance = BigDecimal.valueOf(0);

    @Column(name = "owner_login")
    private String ownerLogin;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Account() {}

    public Account(int userDefinedId, String login){
        this.userDefinedId = userDefinedId;
        ownerLogin = login;
    }

    public int getUserDefinedId() {
        return userDefinedId;
    }

    public BigDecimal getBalance() { return balance; }

    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String login){ this.ownerLogin = login; }

    public void setUserDefinedId(int id){this.userDefinedId = id;}
}
