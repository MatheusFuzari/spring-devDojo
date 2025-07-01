package com.example.users_microservice.services;

import com.example.users_microservice.common.ProfileUtils;
import com.example.users_microservice.common.UserProfileUtils;
import com.example.users_microservice.common.UserUtils;
import com.example.users_microservice.domain.User;
import com.example.users_microservice.domain.UserProfile;
import com.example.users_microservice.repository.UserProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserProfileServiceTest {

    @InjectMocks
    private UserProfileService service;

    @Mock
    private UserProfileRepository repository;

    private List<UserProfile> userProfileList;
    private List<User> userList;

    @InjectMocks
    private UserProfileUtils userProfileUtils;

    @Spy // It's like an autowired for mockito dependencies outside spring boot context.
    private UserUtils userUtils;

    @Spy
    private ProfileUtils profileUtils;

    @BeforeEach
    void init() {
        userProfileList = userProfileUtils.userProfileList();
    }

    @Test
    @DisplayName("findAll() returns a list of all users profiles")
    @Order(1)
    void findAll_ReturnsAllUserProfiles_WhenSuccessful(){
        BDDMockito.when(repository.findAll()).thenReturn(userProfileList);

        var userProfiles = service.findAll();

        Assertions.assertThat(userProfiles).isNotNull().hasSameElementsAs(userProfileList);

        userProfiles.forEach(userProfile -> Assertions.assertThat(userProfile).hasNoNullFieldsOrProperties());
    }

    @Test
    @DisplayName("findAllUsersByProfileId() returns a list of all users by profile id")
    @Order(2)
    void findAllUsersByProfileId_ReturnsAllUserWithProfileId_WhenSuccessful(){
        var profileId = 99L;
        var usersByProfile = this.userProfileList.stream()
                .filter(userProfile -> userProfile.getProfile().getId() == profileId)
                .map(UserProfile::getUser)
                .toList();

        BDDMockito.when(repository.findAllUsersByProfileId(profileId)).thenReturn(usersByProfile);

        var users = service.findAllUsersByProfileId(profileId);

        Assertions.assertThat(users).hasSize(1).isNotNull().hasSameElementsAs(usersByProfile);
        users.forEach(user -> Assertions.assertThat(user).hasNoNullFieldsOrProperties());
    }
}