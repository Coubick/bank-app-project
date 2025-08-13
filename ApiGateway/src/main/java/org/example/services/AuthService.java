package org.example.services;

import org.example.DTO.LoginAndPasswordDTO;
import org.example.security.JwtCore;
import org.example.security.JwtResponse;
import org.example.user.GatewayUserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {
    private final TokenBlackListService tokenBlackListService;
    private AuthenticationManager authenticationManager;
    private JwtCore jwtCore;

    public AuthService(TokenBlackListService tokenBlackListService,
                          AuthenticationManager authenticationManager,
                          JwtCore jwtCore) {
        this.tokenBlackListService = tokenBlackListService;
        this.authenticationManager = authenticationManager;
        this.jwtCore = jwtCore;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtCore(JwtCore jwtCore) {
        this.jwtCore = jwtCore;
    }

    public JwtResponse signIn(LoginAndPasswordDTO loginAndPasswordDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginAndPasswordDTO.getLogin(),
                        loginAndPasswordDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return new JwtResponse(jwtCore.generateToken((GatewayUserDetailsImpl) userDetails));
    }


    public String logout(String token) {
        Date expiration = jwtCore.extractExpiration(token);
        tokenBlackListService.blacklistToken(token, expiration);
        SecurityContextHolder.clearContext();
        return "Logged out";
    }
}
