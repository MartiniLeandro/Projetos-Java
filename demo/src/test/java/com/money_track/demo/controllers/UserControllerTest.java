package com.money_track.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.money_track.demo.entities.DTO.UserDTO;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.Roles;
import com.money_track.demo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private UserDTO userDTO,adminDTO;

    @BeforeEach
    void setup(){
        user = new User("user","702.413.770-30","user@email.com","user123", Roles.ROLE_USER);
        user.setId(1L);
        admin = new User("admin","730.136.230-71","admin@email.com","admin123", Roles.ROLE_ADMIN);
        admin.setId(2L);

        userDTO = new UserDTO(user);
        adminDTO = new UserDTO(admin);
    }

    @DisplayName("test find all users SUCCESS")
    @Test
    void testFindAllUsersSuccess() throws Exception {

        when(userService.findAllUsers()).thenReturn(List.of(userDTO,adminDTO));

        mockMvc.perform(get("/admin/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("user@email.com"))
                .andExpect(jsonPath("$[1].email").value("admin@email.com"));
    }

    @DisplayName("test find user byId SUCCESS")
    @Test
    void testFindUserByIdSuccess() throws Exception {

        when(userService.findUserById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/admin/users/{id}",1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@email.com"))
                .andExpect(jsonPath("$.name").value("user"));
    }

    @DisplayName("test find user byId FAILED")
    @Test
    void testFindUserByIdFailed(){

    }

    @DisplayName("test create user SUCCESS")
    @Test
    void testCreateUserSuccess() throws Exception{
        when(userService.createUser(any(User.class))).thenReturn(adminDTO);

        mockMvc.perform(post("/admin/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@email.com"))
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"));
    }

    @DisplayName("test create user FAILED")
    @Test
    void testCreateUserFailed(){

    }

    @DisplayName("test update user SUCCESS")
    @Test
    void testUpdateUserSuccess() throws Exception{
        Long id = 2L;
        when(userService.updateUser(any(User.class), Mockito.eq(id))).thenReturn(adminDTO);

        mockMvc.perform(put("/admin/users/update/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminDTO)))
                .andDo(print())
                .andExpect(jsonPath("$.email").value("admin@email.com"))
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"));
    }

    @DisplayName("test delete user SUCCESS")
    @Test
    void testDeleteUserSuccess() throws Exception{

        doNothing().when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/admin/users/delete/{id}",1L))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
