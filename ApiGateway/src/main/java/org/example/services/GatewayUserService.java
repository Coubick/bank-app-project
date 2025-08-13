package org.example.services;

import org.example.user.GatewayUserDetailsImpl;
import org.example.repositories.GatewayUserRepository;
import org.example.user.GatewayUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GatewayUserService implements UserDetailsService {
    private final GatewayUserRepository gatewayUserRepository;

    @Autowired
    public GatewayUserService(GatewayUserRepository userRepository){
        this.gatewayUserRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<GatewayUser> user = Optional.ofNullable(gatewayUserRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User %s not found", login)
        )));

        return GatewayUserDetailsImpl.build(user.orElse(null));
    }

    public GatewayUser findByLogin(String login){
        return gatewayUserRepository.findByLogin(login).orElse(null);
    }
}
