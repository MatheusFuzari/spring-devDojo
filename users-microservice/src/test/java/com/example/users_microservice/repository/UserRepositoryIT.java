package com.example.users_microservice.repository;

import com.example.users_microservice.config.IntegrationTestConfig;
import com.example.users_microservice.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Transactional(propagation = Propagation.NOT_SUPPORTED)
class UserRepositoryIT extends IntegrationTestConfig {

    @Autowired
    private UserRepository repository;

    @Test
    @Order(1)
    @DisplayName("save creates an user when successful")
    void save_CreatesUser_WhenSuccessful() {
        var userToSave = User.builder().firstName("Yoichi").lastName("Isagi").email("yoichi.isagi@fromblue.com").build();
        var userCreated = repository.save(userToSave);

        Assertions.assertThat(userCreated).hasNoNullFieldsOrProperties();
        Assertions.assertThat(userCreated.getId()).isNotNull().isPositive();
    }

    @Test
    @Order(2)
    @DisplayName("findAll returns a list of users")
    @Sql("/sql/users/init_one_user.sql")
    void findAll_ReturnAllUsers_WhenSuccessful() {
        var users = repository.findAll();
        Assertions.assertThat(users).isNotEmpty();
    }
}