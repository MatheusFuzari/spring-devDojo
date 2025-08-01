package com.example.users_microservice.mapper;

import com.example.users_microservice.annotation.EncodedMapping;
import com.example.users_microservice.domain.User;
import com.example.users_microservice.dto.request.PostUserRequestDTO;
import com.example.users_microservice.dto.request.PutUserRequestDTO;
import com.example.users_microservice.dto.response.GetUserResponseDTO;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
    uses = PasswordEncoderMapper.class)
public interface UserMapper {

  @Mapping(target = "roles", constant = "USER")
  @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
  User toUser(PostUserRequestDTO postRequest);

  User toUser(PutUserRequestDTO putRequest);

  GetUserResponseDTO toUserGetResponse(User user);

  List<GetUserResponseDTO> toUserGetResponseList(List<User> userList);

  @Mapping(target = "password", source = "rawPassword", qualifiedBy = EncodedMapping.class)
  @Mapping(target = "roles", source = "savedUser.roles")
  @Mapping(target = "id", source = "userToUpdate.id")
  @Mapping(target = "firstName", source = "userToUpdate.firstName")
  @Mapping(target = "lastName", source = "userToUpdate.lastName")
  @Mapping(target = "email", source = "userToUpdate.email")
  User toUserWithPasswordAndRoles(User userToUpdate, String rawPassword, User savedUser);

  @AfterMapping
  default void setPasswordIfNull(@MappingTarget User user, String rawPassword, User savedUser) {
    if (rawPassword == null) {
      user.setPassword(savedUser.getPassword());
    }
  }
}
