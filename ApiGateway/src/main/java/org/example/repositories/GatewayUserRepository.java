package org.example.repositories;

import org.example.user.GatewayUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GatewayUserRepository extends JpaRepository<GatewayUser, Integer> {
    Optional<GatewayUser> findByLogin(String login);
    boolean existsByLogin(String login);
}