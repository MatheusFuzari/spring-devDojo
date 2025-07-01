package com.example.users_microservice.repository;

import com.example.users_microservice.config.IntegrationTestConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserProfileRepositoryIT extends IntegrationTestConfig {

    @Autowired
    private UserProfileRepository repository;

    @Test
    @DisplayName("findAll returns a list of users by profile id")
    @Order(1)
    @Sql("/sql/user_profiles/init_user_profile_2_users_1_profile.sql")
    void findAllByProfileId_ReturnsAllUsersByProfileId_WhenProfileIdExists() {
        var profileId = 1L;
        var users = repository.findAllUsersByProfileId(profileId);

        Assertions.assertThat(users).isNotEmpty().hasSize(2).doesNotContainNull();

        users.forEach(user -> Assertions.assertThat(user).hasNoNullFieldsOrProperties());
    }

}