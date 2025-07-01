package com.example.users_microservice.controller;

import com.example.users_microservice.domain.User;
import com.example.users_microservice.dto.request.PostUserRequestDTO;
import com.example.users_microservice.dto.request.PutUserRequestDTO;
import com.example.users_microservice.dto.response.GetUserResponseDTO;
import com.example.users_microservice.mapper.UserMapper;
import com.example.users_microservice.services.UserService;
import com.exemple.dev_dojo.DefaultErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User API", description = "User related endpoints")
public class UserController {

    private final UserService service;

    private final UserMapper MAPPER = UserMapper.MAPPER;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all users", description = "Get all users available in the system"
            , responses = {
                @ApiResponse(
                        description = "List all users",
                        responseCode = "200",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = GetUserResponseDTO.class)))
                )
    })
    public ResponseEntity<List<GetUserResponseDTO>> getUsers(@RequestParam(required = false) String name) {
        return ResponseEntity.status(HttpStatus.OK).body(MAPPER.toUserGetResponseList(service.findAll(name)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id"
            ,
            parameters = {
                @Parameter(
                        in = ParameterIn.PATH,
                        name = "id",
                        description = "User Id",
                        example = "1"
                )
            },
            responses = {
            @ApiResponse(
                    description = "Get user by it's id",
                    responseCode = "200",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GetUserResponseDTO.class))
            ),
            @ApiResponse(
                    description = "User Not Found",
                    responseCode = "404",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DefaultErrorMessage.class))
            )
    })
    public ResponseEntity<GetUserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(MAPPER.toUserGetResponse(service.findByIdOrThrowNotFound(id)));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create user"
            , responses = {
            @ApiResponse(
                    description = "Created user",
                    responseCode = "201",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                    description = "User bad request",
                    responseCode = "400",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DefaultErrorMessage.class))
            )
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> createUser(@RequestBody @Valid PostUserRequestDTO request){
        var userToSave = MAPPER.toUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(userToSave));
    }


    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Update User",
            responses = {
                    @ApiResponse(
                            description = "Updated User",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Id Not Found",
                            responseCode = "404",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DefaultErrorMessage.class))
                    ),
                    @ApiResponse(
                            description = "E-mail already in use",
                            responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DefaultErrorMessage.class))
                    )
            }
    )
    public ResponseEntity<Void> updateUser(@RequestBody @Valid PutUserRequestDTO request){
        log.info("PUT request: {}", request);

        var userToUpdate = MAPPER.toUser(request);

        service.update(userToUpdate);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Update User",
            parameters = {
              @Parameter(
                      in = ParameterIn.PATH,
                      description = "User Id",
                      example = "1"
              )
            },
            responses = {
                    @ApiResponse(
                            description = "Deleted User",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Id Not Found",
                            responseCode = "404",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DefaultErrorMessage.class))
                    )
            }
    )
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        service.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
