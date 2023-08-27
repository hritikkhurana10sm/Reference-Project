package com.amanboora.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "auth_user", indexes = {
        @Index(name = "username_index", columnList = "username"),
        @Index(name = "email_index", columnList = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser {

    @Id
    private String id;

    @Column(name = "username", unique = true, nullable = false, updatable = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false, updatable = true)
    private String email;

    @Column(name = "activation_id", nullable = true)
    private String activationId;

    @Column(name = "register_time", nullable = false)
    private LocalDateTime registerTIme;

    @Column(name="reset_id", nullable = true)
    private String resetId;
}
