package com.example.users_microservice.mapper;

import com.example.users_microservice.domain.Profile;
import com.example.users_microservice.dto.request.PostProfileRequestDTO;
import com.example.users_microservice.dto.response.GetProfileResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProfileMapper {

    ProfileMapper MAPPER = Mappers.getMapper( ProfileMapper.class );

    GetProfileResponseDTO toGetResponse(Profile profile);

    Profile toProfile(PostProfileRequestDTO postRequest);

    List<GetProfileResponseDTO> toGetResponseList(List<Profile> profile);

}