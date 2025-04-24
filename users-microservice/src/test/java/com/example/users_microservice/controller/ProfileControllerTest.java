package com.example.users_microservice.controller;

import com.example.users_microservice.common.FileUtils;
import com.example.users_microservice.common.ProfileUtils;
import com.example.users_microservice.domain.Profile;
import com.example.users_microservice.repository.ProfileRepository;
import com.example.users_microservice.services.ProfileService;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(ProfileController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = {Profile.class, ProfileRepository.class, ProfileController.class, ProfileService.class, FileUtils.class, ProfileUtils.class})
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfileService service;

    @Autowired
    FileUtils utils;

    @Autowired
    ProfileUtils profileUtils;

    List<Profile> profileList;

    @BeforeEach
    void init() {
        profileList = profileUtils.newProfilesList();
    }


    @Test
    @DisplayName("GET /v1/profiles returns a list of profiles when successful")
    @Order(1)
    void findAll_ReturnsAllProfiles_WhenSuccessful() throws Exception {
        BDDMockito.when(service.getAllProfiles()).thenReturn(profileList);

        var response = utils.readResourceFile("/profiles/get-all-profiles-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/profiles"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST /v1/profiles creates an user when successful")
    @Order(2)
    void save_ReturnsNewProfile_WhenSuccessful() throws Exception{
        var request = utils.readResourceFile("/profiles/post-request-profiles-200.json");
        var response = utils.readResourceFile("/profiles/post-response-profiles-201.json");

        var createdUser = new Profile().withId(1L).withName("Admin").withDescription("Admin Profile");

        BDDMockito.when(service.createProfile(BDDMockito.any(Profile.class))).thenReturn(createdUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }
}