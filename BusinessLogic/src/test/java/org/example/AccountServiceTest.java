package org.example;

import org.example.account_dao.Account;
import org.example.account_dao.AccountRepository;
import org.example.enums.DepositResult;
import org.example.enums.WithdrawResult;
import org.example.operations_dao.OperationRepository;
import org.example.producers.AccountEventProducer;
import org.example.services.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    public void deposit_DepositCorrectAmount_SuccessfullyDep(){
        Account account = new Account(123, "bob");
        account.setId(0);
        when(accountRepository.findByUserDefinedId(123)).thenReturn(account);

        accountService.deposit(123, BigDecimal.valueOf(100));

        assertEquals(BigDecimal.valueOf(100), account.getBalance());
        verify(accountRepository).save(account);
    }

    @Test
    public void deposit_DepositIncorrectAmount_IncorrectAmountError(){
        DepositResult result = accountService.deposit(1234, BigDecimal.ZERO);
        assertEquals(DepositResult.IncorrectAmount, result);
        verifyNoInteractions(accountRepository);
    }

    @Test void withdraw_WithdrawCorrectAmountFromAccount_Success(){
        Account account = new Account(1234, "joey");
        account.setId(1);
        account.setBalance(BigDecimal.valueOf(100));
        when(accountRepository.findByUserDefinedId(1)).thenReturn(account);

        accountService.withdraw(1, BigDecimal.valueOf(100));

        assertEquals(account.getBalance(), BigDecimal.valueOf(0));
        verify(accountRepository).save(account);
    }

    @Test
    public void withdraw_WithdrawMoreThanAvailable_InsufficientFunds(){
        Account account = new Account(1234, "joey");
        account.setId(1);
        account.setBalance(BigDecimal.valueOf(100));

        when(accountRepository.findByUserDefinedId(1234)).thenReturn(account);

        WithdrawResult result = accountService.withdraw(1234, BigDecimal.valueOf(1000));

        assertEquals(WithdrawResult.InsufficientFunds, result);
        verify(accountRepository, never()).save(account);
    }

}
