package org.example.controllers;

import org.example.DTO.AccountInfoDTO;
import org.example.services.AccountServiceGateway;
import org.example.DTO.FriendsWithAccountsDTO;
import org.example.services.UserService;
import org.example.DTO.FullUserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.naming.ServiceUnavailableException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/gateway/client")
public class ClientController {
    private final UserService userService;
    private final AccountServiceGateway accountServiceGateway;

    public ClientController(UserService userService, AccountServiceGateway accountServiceGateway) {
        this.userService = userService;
        this.accountServiceGateway = accountServiceGateway;
    }

    @PostMapping("/create-new-account")
    public ResponseEntity<String> createNewAccount(@RequestBody AccountInfoDTO accountInfoDTO) throws ServiceUnavailableException {
        accountServiceGateway.forwardAccountToMainApp(accountInfoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/get-info-about-client")
    public ResponseEntity<FullUserDTO> getUserInfoAboutClient(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserInfo(authentication.getName()));
    }

    @PutMapping("/add-friend/{friendsLogin}")
    public ResponseEntity<String> addFriend(@PathVariable String friendsLogin, Authentication authentication) {
        HttpStatus status = userService.addFriend(authentication, friendsLogin);
        return switch (status) {
            case HttpStatus.OK -> ResponseEntity.ok("Friend added successfully");
            case HttpStatus.NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with login "+ friendsLogin + " not found");
            case HttpStatus.CONFLICT -> ResponseEntity.status(HttpStatus.CONFLICT).body("You are already friends");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        };
    }

    @DeleteMapping("/delete-friend/{friendsLogin}")
    public ResponseEntity<String> deleteFriend(@PathVariable String friendsLogin, Authentication authentication) {
        HttpStatus status = userService.removeFriend(authentication, friendsLogin);
        return switch (status){
            case HttpStatus.OK -> ResponseEntity.ok("Friend deleted successfully");
            case HttpStatus.NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with login "+ friendsLogin + " not found");
            case HttpStatus.CONFLICT -> ResponseEntity.status(HttpStatus.CONFLICT).body("You don't have this user in your friends list");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        };
    }

    @GetMapping("/my-accounts")
    public ResponseEntity<List<AccountInfoDTO>> getMyAccounts(Authentication authentication) {
        return ResponseEntity.ok(accountServiceGateway.getMyAccounts(authentication));
    }

    @GetMapping("/my-account-by-id/{id}")
    public ResponseEntity<List<AccountInfoDTO>> getMyAccountById(@PathVariable int id, Authentication authentication) {
        return ResponseEntity.ok(accountServiceGateway.getMyAccountById(id, authentication));
    }

    @PutMapping("/my-account/{id}/deposit")
    public ResponseEntity<?> depositAccount(@PathVariable int id, @RequestParam BigDecimal amount, Authentication authentication) {
        accountServiceGateway.depositAccount(id, amount, authentication);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/my-account/{id}/withdraw")
    public ResponseEntity<?> withdrawAccount(@PathVariable int id, @RequestParam BigDecimal amount, Authentication authentication) {
        accountServiceGateway.withdrawAccount(id, authentication, amount);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{senderAccountId}/transfer/{receiverAccountId}")
    public ResponseEntity<?> transfer(
            @PathVariable int senderAccountId,
            @PathVariable int receiverAccountId,
            @RequestParam BigDecimal amount,
            Authentication authentication) {
        accountServiceGateway.transfer(senderAccountId, receiverAccountId, amount, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/friends-with-accounts")
    public ResponseEntity<List<FriendsWithAccountsDTO>> getFriendsWithAccounts(Authentication authentication) {
        return ResponseEntity.ok(userService.getFriendsWithAccounts(authentication));
    }
}
