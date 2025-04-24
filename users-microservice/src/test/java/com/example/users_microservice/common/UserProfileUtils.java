package com.example.users_microservice.common;

import com.example.users_microservice.domain.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserProfileUtils {

    private final ProfileUtils profileUtils;
    private final UserUtils userUtils;

    public List<UserProfile> userProfileList() {
        var regularUserProfile = newUserProfileSaved();

        return List.of(regularUserProfile);
    }

    public UserProfile newUserProfileToSave() {
        return UserProfile.builder()
                .user(userUtils.newUserSaved())
                .profile(profileUtils.newProfileSaved())
                .build();
    }

    public UserProfile newUserProfileSaved() {
        return UserProfile.builder()
                .id(99L)
                .user(userUtils.newUserSaved())
                .profile(profileUtils.newProfileSaved())
                .build();
    }
}
