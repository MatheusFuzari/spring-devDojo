package com.example.users_microservice.mapper;

import com.example.users_microservice.domain.User;
import com.example.users_microservice.domain.UserProfile;
import com.example.users_microservice.dto.request.PostUserRequestDTO;
import com.example.users_microservice.dto.request.PutUserRequestDTO;
import com.example.users_microservice.dto.response.GetUserProfileResponseDTO;
import com.example.users_microservice.dto.response.GetUserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {

    UserProfileMapper MAPPER = Mappers.getMapper( UserProfileMapper.class );

    List<GetUserProfileResponseDTO> toGetUserProfileResponseDTO(List<UserProfile> userProfiles);

    List<GetUserResponseDTO> toGetUserResponseDTO(List<User> userProfiles);
}
