package com.example.users_microservice.domain;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @EqualsAndHashCode.Include
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    public String getFullName() {
        return firstName+" "+lastName;
    }
}
