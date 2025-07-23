package com.example.users_microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

@With
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User implements UserDetails {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "User's id", example = "99")
    private Long id;
    @Column(nullable = false)
    @Schema(description = "User's first name", example = "Yoichi")
    private String firstName;
    @Column(nullable = false)
    @Schema(description = "User's last name", example = "Isagi")
    private String lastName;
    @Column(nullable = false, unique = true)
    @Schema(description = "User's e-mail. Must be unique", example = "yoichi.isagi@frombluelock.com")
    private String email;
    @Column(nullable = false)
    @Schema(description = "User's password", example = "password123")
    private String password;
    @Column(nullable = false)
    @Schema(description = "User's roles", example = "password123")
    private String roles;

    @JsonIgnore
    public String getFullName() {
        return firstName+" "+lastName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
