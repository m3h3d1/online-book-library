package com.mehedi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Email;

@Entity
@Table(name="User")
@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "first_name",nullable = false)
    @NotEmpty
    private String firstName;
    @Column(name = "last_name",nullable = false)
    @NotEmpty
    private String lastName;
    @Column(name = "email", nullable = false, unique = true)
    @NotEmpty
    @Email
    private String email;
    @Column(name = "password", nullable = false)
    @NotEmpty
    private String password;
    @Column(name = "address", nullable = false)
    @NotEmpty
    private String address;
    public enum Role {
        CUSTOMER,
        ADMIN
    }
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
}