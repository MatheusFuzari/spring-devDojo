package com.example.users_microservice.repository;

import com.example.users_microservice.domain.User;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class UserData {

    private final List<User> users = new ArrayList<>();

    {
        var guts = User.builder().id(1L).firstName("Guts").lastName("").email("guts@fromberserk.com").build();
        var itadori = User.builder().id(2L).firstName("Yuji").lastName("Itadori").email("yuji.itadori@fromjujusu.com").build();
        var kaneki = User.builder().id(3L).firstName("Ken").lastName("Kaneki").email("Ken.Kaneki@fromghoul.com").build();

        this.users.addAll(List.of(guts, itadori, kaneki));
    }
}
