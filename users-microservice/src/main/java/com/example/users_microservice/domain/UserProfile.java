package com.example.users_microservice.domain;

import jakarta.persistence.*;
import lombok.*;

@With
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserProfile {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Profile profile;

    @ManyToOne(optional = false)
    private User user;

}
