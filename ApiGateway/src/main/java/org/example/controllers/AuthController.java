package org.example.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.example.security.JwtCore;
import org.example.services.AuthService;
import org.example.repositories.GatewayUserRepository;
import org.example.user.AppUser;
import org.example.DTO.LoginAndPasswordDTO;
import org.example.DTO.FullUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final RestTemplate restTemplate;
    private final String MAIN_APP_URL = "http://localhost:8080/api/v3/bank_system/users";
    private GatewayUserRepository gatewayUserRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtCore jwtCore;

    @ExceptionHandler
    private ResponseEntity<?> forwardUserToMainApp(FullUserDTO user) {
        AppUser appUser = new AppUser();
        appUser.setLogin(user.getLogin());
        appUser.setName(user.getUserName());
        appUser.setAge(user.getAge());
        appUser.setGender(user.getGender());
        appUser.setHairColor(user.getHairColor());

        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(
                    MAIN_APP_URL,
                    appUser,
                    Void.class
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getResponseBodyAsString());
        }
    }

    public AuthController(GatewayUserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtCore jwtCore,
                          RestTemplate restTemplate, AuthService authService) {
        this.gatewayUserRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtCore = jwtCore;
        this.restTemplate = restTemplate;
        this.authService = authService;
    }

    @Autowired
    public void setGatewayUserRepository(GatewayUserRepository gatewayUserRepository) {
        this.gatewayUserRepository = gatewayUserRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtCore(JwtCore jwtCore) {
        this.jwtCore = jwtCore;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody LoginAndPasswordDTO loginAndPasswordDTO) {
        try {
            return ResponseEntity.ok(authService.signIn(loginAndPasswordDTO));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("No token");
        }

        try {
            String token = header.substring(7);
            return ResponseEntity.ok(authService.logout(token));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Logout error");
        }    }

}
