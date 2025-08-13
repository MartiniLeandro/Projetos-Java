package com.money_track.demo.repositories;

import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user,admin;

    @BeforeEach
    void setup(){
        user = new User("user","702.413.770-30","user@email.com","user123", Roles.ROLE_USER);
        admin = new User("admin","730.136.230-71","admin@email.com","admin123", Roles.ROLE_ADMIN);
    }

    @Test
    void createUser(){
        userRepository.save(user);
        userRepository.save(admin);

        assertTrue(userRepository.findById(user.getId()).isPresent());
        assertTrue(userRepository.findById(admin.getId()).isPresent());
    }
}

