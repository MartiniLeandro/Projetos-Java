package com.money_track.demo.repositories;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.Launch;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.Roles;
import com.money_track.demo.entities.enums.TypeValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class LaunchRepositoryTest {

    @Autowired
    private LaunchRepository launchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Launch launch;
    private User user;
    private Category category;

    @BeforeEach
    void setup(){
        user = new User("user@email.com","user123", Roles.ROLE_USER);
        category = new Category("salário", TypeValue.REVENUE);
        launch = new Launch("teste",category,1800.0, LocalDate.of(2025,3,12),user);
    }

    @Test
    void createLaunch(){
        userRepository.save(user);
        categoryRepository.save(category);
        launchRepository.save(launch);

        assertTrue(launchRepository.findById(launch.getId()).isPresent());
    }
}

