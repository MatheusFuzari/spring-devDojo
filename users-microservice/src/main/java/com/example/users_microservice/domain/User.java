package com.example.users_microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@With
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

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

    @JsonIgnore
    public String getFullName() {
        return firstName+" "+lastName;
    }
}
