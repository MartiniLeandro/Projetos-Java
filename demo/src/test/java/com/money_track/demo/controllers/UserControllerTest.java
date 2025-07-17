package com.money_track.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.money_track.demo.entities.DTO.UserDTO;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.Roles;
import com.money_track.demo.repositories.UserRepository;
import com.money_track.demo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User user,admin;

    @BeforeEach
    void setup(){
        user = new User(1L,"user","702.413.770-30","user@email.com","user123", Roles.ROLE_USER);
        admin = new User(2L,"admin","730.136.230-71","admin@email.com","admin123", Roles.ROLE_ADMIN);
    }

    @DisplayName("test find all users SUCCESS")
    @Test
    void testFindAllUsersSuccess() throws Exception {

        when(userService.findAllUsers()).thenReturn(List.of(new UserDTO(user),new UserDTO(admin)));

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("user@email.com"))
                .andExpect(jsonPath("$[1].email").value("admin@email.com"));
    }
}
