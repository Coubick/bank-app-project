package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.account_dao.Account;
import org.example.services.AccountService;
import org.example.dto.AccountDTO;
import org.example.enums.DepositResult;
import org.example.enums.TransferResult;
import org.example.enums.WithdrawResult;
import org.example.mappers.AccountMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Accounts", description = "Account management API")
@RestController
@RequestMapping("/api/v3/bank_system/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    public AccountController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @Operation(summary = "Create new account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created"),
            @ApiResponse(responseCode = "409", description = "Account with this ID already exists")
    })
    @PostMapping
    public ResponseEntity<Void> addAccount(@RequestBody AccountDTO accountDto) {
        if (accountService.accountExists(accountDto.getUserDefinedId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        accountService.addAccount(accountMapper.toEntity(accountDto));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Get account balance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance returned"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> checkBalance(@PathVariable int id) {
        if (!accountService.accountExists(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(accountService.checkBalance(id));
    }

    @Operation(summary = "Deposit account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit succeed"),
            @ApiResponse(responseCode = "404", description = "Account id not found")
    })
    @PutMapping("/{id}/deposit")
    public ResponseEntity<Object> deposit(@PathVariable int id, @RequestParam BigDecimal amount) {
        DepositResult res = accountService.deposit(id, amount);
        if (res == DepositResult.AccountDoesNotExist)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (res == DepositResult.IncorrectAmount)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Withdraw money from account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Withdrawal succeed"),
            @ApiResponse(responseCode = "404", description = "Account id not found"),
            @ApiResponse(responseCode = "409", description = "Incorrect amount of money")
    })

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<Object> withdraw(@PathVariable int id, @RequestParam BigDecimal amount) {
        WithdrawResult res = accountService.withdraw(id, amount);
        if (res == WithdrawResult.AccountNotFound)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (res == WithdrawResult.IncorrectAmount)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Transfer money")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer succeed"),
            @ApiResponse(responseCode = "404", description = "Account id not found"),
            @ApiResponse(responseCode = "409", description = "Incorrect amount of money")
    })

    @PutMapping("/{senderId}/transfer/{receiverId}")
    public ResponseEntity<Object> transfer(@PathVariable int senderId, @PathVariable int receiverId, @RequestParam double amount) {
        TransferResult res = accountService.transfer(senderId, receiverId, amount);
        if (res == TransferResult.SenderAccountDoesNotExist)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if (res == TransferResult.NotEnoughMoney)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accountMapper.toDtoList(accounts));
    }
}