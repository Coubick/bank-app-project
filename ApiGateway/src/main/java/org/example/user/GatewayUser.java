package org.example.user;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "gateway_user")
public class GatewayUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String login;
    private String role;
    private String password;
}
