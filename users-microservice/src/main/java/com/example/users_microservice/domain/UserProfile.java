package com.example.users_microservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@With
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@NamedEntityGraph(
        name = "UserProfile.fullDetails",
        attributeNodes = {
                @NamedAttributeNode("user"),
                @NamedAttributeNode("profile")
        }
)
public class UserProfile {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Profile profile;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

}
