package com.example.users_microservice.services;

import com.example.users_microservice.common.ProfileUtils;
import com.example.users_microservice.domain.Profile;
import com.example.users_microservice.repository.ProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileServiceTest {

    @InjectMocks
    private ProfileService service;

    @Mock
    private ProfileRepository repository;

    @InjectMocks
    private ProfileUtils profileUtils;

    List<Profile> profileList = new ArrayList<>();

    @BeforeEach
    void init(){
        profileList = profileUtils.newProfiles();
    }

    @Test
    @DisplayName("getAllProfiles return a list of profiles")
    @Order(1)
    void getAllProfiles_ReturnProfileList_WhenSuccessful() {
        BDDMockito.when(repository.findAll()).thenReturn(profileList);

        var profiles = service.getAllProfiles();

        Assertions.assertThat(profiles).isNotEmpty().hasSameElementsAs(profileList);
    }

    @Test
    @DisplayName("getAllProfilesPaginated return a paginated list of profiles")
    @Order(2)
    void getAllProfilesPaginated_ReturnPaginatedList_WhenSuccessful() {
        var pageRequest = PageRequest.of(0, profileList.size());
        var paginatedReturn = new PageImpl<>(profileList, pageRequest, profileList.size());
        BDDMockito.when(repository.findAll(BDDMockito.any(Pageable.class))).thenReturn(paginatedReturn);

        var profilePaginated = service.getAllProfilesPaginated(pageRequest);

        Assertions.assertThat(profilePaginated).isNotEmpty().hasSameElementsAs(profileList);
    }

    @Test
    @DisplayName("createProfile return a profile")
    @Order(3)
    void createProfile_ReturnProfile_WhenSuccessful() {
        var profileToCreate = new Profile().withId(5L).withName("Tertiary").withDescription("Tertiary profile");
        BDDMockito.when(repository.save(BDDMockito.any(Profile.class))).thenReturn(profileToCreate);

        var profileCreate = service.createProfile(profileToCreate);

        Assertions.assertThat(profileCreate).hasNoNullFieldsOrProperties();
    }
}