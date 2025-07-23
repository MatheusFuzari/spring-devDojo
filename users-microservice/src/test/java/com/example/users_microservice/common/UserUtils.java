package com.example.users_microservice.common;

import com.example.users_microservice.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserUtils {

    public List<User> newUsersList() {
        var midoriya_izuku = new User()
                .withId(1L)
                .withEmail("izuku.midoriya@frombnha.com")
                .withFirstName("Izuku")
                .withLastName("Midoriya")
                .withPassword("{bcrypt}$2a$10$LaNgnLoLcy8Y/vKvn3nmT.kNk1qXwV3jdwzHyblWulpzD3zatOZci")
                .withRoles("USER");

        var kamado_tanjiro = new User()
                .withId(2L)
                .withEmail("tanjiro.kamado@fromkimetsu.com")
                .withFirstName("Tanjiro")
                .withLastName("Kamado")
                .withPassword("{bcrypt}$2a$10$LaNgnLoLcy8Y/vKvn3nmT.kNk1qXwV3jdwzHyblWulpzD3zatOZci")
                .withRoles("USER");

        var guts = new User()
                .withId(3L)
                .withEmail("guts.berserk@frombersek.com")
                .withFirstName("Guts")
                .withLastName("Berserk")
                .withPassword("{bcrypt}$2a$10$LaNgnLoLcy8Y/vKvn3nmT.kNk1qXwV3jdwzHyblWulpzD3zatOZci")
                .withRoles("USER");

        var yoichi_isagi = new User()
                .withId(4L)
                .withEmail("isagi.yoichi@fromblue.com")
                .withFirstName("Isagi")
                .withLastName("Yoichi")
                .withPassword("{bcrypt}$2a$10$LaNgnLoLcy8Y/vKvn3nmT.kNk1qXwV3jdwzHyblWulpzD3zatOZci")
                .withRoles("USER");

        return List.of(yoichi_isagi, guts, kamado_tanjiro, midoriya_izuku);
    }

    public User newUserToSave(){
        return new User()
                .withEmail("Hyouma.Chigiri@FromBlue.com")
                .withFirstName("Hyouma")
                .withLastName("Chigiri")
                .withPassword("{bcrypt}$2a$10$LaNgnLoLcy8Y/vKvn3nmT.kNk1qXwV3jdwzHyblWulpzD3zatOZci")
                .withRoles("USER");
    }

    public User newUserSaved(){
        return new User()
                .withId(99L)
                .withEmail("Hyouma.Chigiri@FromBlue.com")
                .withFirstName("Hyouma")
                .withLastName("Chigiri")
                .withPassword("{bcrypt}$2a$10$LaNgnLoLcy8Y/vKvn3nmT.kNk1qXwV3jdwzHyblWulpzD3zatOZci")
                .withRoles("USER");
    }
}
