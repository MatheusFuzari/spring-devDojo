package com.example.users_microservice.mapper;

import com.example.users_microservice.annotation.EncodedMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderMapper {

    private final PasswordEncoder passwordEncoder;

    @EncodedMapping
    public String encode(String rawPassword) {
        return rawPassword == null ? null : passwordEncoder.encode(rawPassword);
    }
}
