package org.example.operations_dao;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "operations")
public class OperationClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_id")
    private int operationId;

    @Column(name = "account_id")
    private int accountId;

    @Column(name = "money_amount")
    private BigDecimal moneyAmount;

    @Column(name = "operation_type")
    private String operationType;

    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public BigDecimal getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(BigDecimal moneyAmount) {
        this.moneyAmount = moneyAmount;
    }
}
