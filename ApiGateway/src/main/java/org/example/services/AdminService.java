package org.example.services;

import jakarta.transaction.Transactional;
import org.example.DTO.AccountInfoDTO;
import org.example.DTO.FullUserDTO;
import org.example.DTO.FullUserDtoMapper;
import org.example.clients.AdminClient;
import org.example.repositories.GatewayUserRepository;
import org.example.user.AppUser;
import org.example.user.GatewayUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import javax.naming.ServiceUnavailableException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final AdminClient adminClient;
    private final GatewayUserService gatewayUserService;
    private final GatewayUserRepository gatewayUserRepository;
    private final FullUserDtoMapper fullUserDtoMapper;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminClient adminClient, GatewayUserRepository userRepository,
                        PasswordEncoder passwordEncoder, GatewayUserService gatewayUserService, FullUserDtoMapper fullUserDtoMapper) {
        this.adminClient = adminClient;
        this.gatewayUserRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.gatewayUserService = gatewayUserService;
        this.fullUserDtoMapper = fullUserDtoMapper;
    }


    @ExceptionHandler
    private void forwardUserToMainApp(FullUserDTO user) throws ServiceUnavailableException {
        AppUser appUser = new AppUser();
        appUser.setLogin(user.getLogin());
        appUser.setName(user.getUserName());
        appUser.setAge(user.getAge());
        appUser.setGender(user.getGender());
        appUser.setHairColor(user.getHairColor());

        try {
            adminClient.post(appUser);
        } catch (HttpClientErrorException e) {
            throw new ServiceUnavailableException("Main application error: " + e.getResponseBodyAsString());
        }
    }

    @Transactional
    public void createUser(FullUserDTO signupRequestUserDTO) throws Exception {
        if (gatewayUserRepository.existsByLogin(signupRequestUserDTO.getLogin())) {
            throw new Exception("Choose other login");
        }

        forwardUserToMainApp(signupRequestUserDTO);

        GatewayUser gatewayUser = new GatewayUser();
        gatewayUser.setLogin(signupRequestUserDTO.getLogin());
        gatewayUser.setPassword(passwordEncoder.encode(signupRequestUserDTO.getPassword()));
        gatewayUser.setRole(signupRequestUserDTO.getRole());
        gatewayUserRepository.save(gatewayUser);
    }

    public List<AccountInfoDTO> getAllAccounts() {
        return adminClient.fetchAllAccounts();
    }

    public List<AccountInfoDTO> getAccountsByLogin(String ownerLogin) throws ServiceUnavailableException {
        try {
            List<AccountInfoDTO> allAccounts = adminClient.fetchAllAccounts();

            if (allAccounts == null || allAccounts.isEmpty()) {
                return Collections.emptyList();
            }

            return allAccounts.stream()
                    .filter(account -> account.getOwnerLogin().equals(ownerLogin))
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException.NotFound ex) {
            throw new NoSuchElementException("Accounts not found for user: " + ownerLogin);
        } catch (HttpClientErrorException ex) {
            throw new ServiceUnavailableException("External service error: " + ex.getResponseBodyAsString());
        }
    }


    public List<AppUser> getFilteredUsers(String gender, String color) {
        return adminClient.fetchFilteredUsers(gender, color);
    }

    public FullUserDTO getUserInfo(String login){
        AppUser appUser = adminClient.fetchUserInfo(login);
        GatewayUser gatewayUser = gatewayUserService.findByLogin(login);
        return fullUserDtoMapper.toFullUserDTO(appUser, gatewayUser);
    }

}
