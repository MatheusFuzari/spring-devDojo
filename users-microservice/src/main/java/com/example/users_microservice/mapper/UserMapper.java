package com.example.users_microservice.mapper;

import com.example.users_microservice.domain.User;
import com.example.users_microservice.dto.request.PostUserRequestDTO;
import com.example.users_microservice.dto.request.PutUserRequestDTO;
import com.example.users_microservice.dto.response.GetUserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper MAPPER = Mappers.getMapper( UserMapper.class );

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
    User toUser(PostUserRequestDTO postRequest);

    User toUser(PutUserRequestDTO putRequest);

    GetUserResponseDTO toUserGetResponse(User user);

    List<GetUserResponseDTO> toUserGetResponseList(List<User> userList);
}
