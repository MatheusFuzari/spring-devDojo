package com.example.users_microservice.common;

import com.example.users_microservice.domain.Profile;
import com.example.users_microservice.domain.User;

import java.util.List;

public class UserUtils {

    public List<User> newProfiles() {
        var midoriya_izuku = new User().withId(1L).withEmail("izuku.midoriya@frombnha.com").withFirstName("Izuku").withLastName("Midoriya");
        var kamado_tanjiro = new User().withId(2L).withEmail("tanjiro.kamado@fromkimetsu.com").withFirstName("Tanjiro").withLastName("Kamado");
        var guts = new User().withId(3L).withEmail("guts.berserk@frombersek.com").withFirstName("Guts").withLastName("Berserk");
        var yoichi_isagi = new User().withId(4L).withEmail("isagi.yoichi@fromblue.com").withFirstName("Isagi").withLastName("Yoichi");

        return List.of(yoichi_isagi, guts, kamado_tanjiro, midoriya_izuku);
    }
}
