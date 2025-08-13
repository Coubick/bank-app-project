package org.example.controllers;

import org.example.DTO.AccountInfoDTO;
import org.example.DTO.FullUserDTO;
import org.example.services.AdminService;
import org.example.user.AppUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.ServiceUnavailableException;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody FullUserDTO signupRequestUserDTO) throws Exception {
        adminService.createUser(signupRequestUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/get-all-accounts")
    public ResponseEntity<List<AccountInfoDTO>> getAllAccounts() {
        return ResponseEntity.ok(adminService.getAllAccounts());
    }

    @GetMapping("/get-accounts-by-login/{login}")
    public ResponseEntity<List<AccountInfoDTO>> getAccountsByLogin(@PathVariable String login) throws ServiceUnavailableException {
        return ResponseEntity.ok(adminService.getAccountsByLogin(login));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<AppUser>> getFilteredUsers(@RequestParam(required = false) String gender, @RequestParam(required = false) String color) {
        return ResponseEntity.ok(adminService.getFilteredUsers(gender, color));
    }

    @GetMapping("/get-info-about-user/{login}")
    public ResponseEntity<FullUserDTO> getUserInfo(@PathVariable String login) {
        return ResponseEntity.ok(adminService.getUserInfo(login));
    }
}