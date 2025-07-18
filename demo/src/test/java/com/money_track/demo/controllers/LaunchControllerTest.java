package com.money_track.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.LaunchDTO;
import com.money_track.demo.entities.Launch;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.Roles;
import com.money_track.demo.entities.enums.TypeValue;
import com.money_track.demo.security.SecurityFilter;
import com.money_track.demo.security.TokenService;
import com.money_track.demo.services.LaunchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(LaunchController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LaunchControllerTest {

    @MockBean
    private LaunchService launchService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private SecurityFilter securityFilter;


    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private Category category1,category2;
    private Launch launch1,launch2;
    private LaunchDTO launch1DTO,launch2DTO;

    @BeforeEach
    void setup(){
        user = new User("user","702.413.770-30","user@email.com","user123", Roles.ROLE_USER);
        category1 = new Category("salary", TypeValue.REVENUE);
        category2 = new Category("school", TypeValue.EXPENSE);

        launch1 = new Launch("salary",category1,1500.0, LocalDate.of(2025,6,1),user);
        launch1.setId(1L);
        launch2 = new Launch("school",category2,500.0, LocalDate.of(2025,6,2),user);
        launch2.setId(2L);

        user.setLaunches(List.of(launch1,launch2));

        launch1DTO = new LaunchDTO(launch1);
        launch2DTO = new LaunchDTO(launch2);
    }

    @DisplayName("test find all launches SUCCESS")
    @Test
    void testFindAllLaunchesSuccess() throws Exception {

        when(launchService.findAllLaunches(Mockito.eq("fake-token"))).thenReturn(List.of(launch1DTO,launch2DTO));

        mockMvc.perform(get("/user/launches")
                        .header("Authorization","fake-token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("salary"))
                .andExpect(jsonPath("$[1].description").value("school"));
    }

    @DisplayName("test find launch by id SUCCESS")
    @Test
    void testFindLaunchByIdSuccess() throws Exception {

        when(launchService.findLaunchById(Mockito.eq("fake-token"),anyLong())).thenReturn(launch2DTO);

        mockMvc.perform(get("/user/launches/{id}",2L)
                .header("Authorization","fake-token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("school"));
    }

    @DisplayName("test create launch SUCCESS")
    @Test
    void testCreateLaunchSuccess() throws Exception{

        when(launchService.createLaunch(any(Launch.class),Mockito.eq("fake-token"))).thenReturn(launch1DTO);

        mockMvc.perform(post("/user/launches/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(launch1DTO))
                .header("Authorization","fake-token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("salary"));
    }

    @DisplayName("test update launch SUCCESS")
    @Test
    void testUpdateLaunchSuccess() throws Exception{
        Launch launch3 = new Launch("academy",category2,180.0, LocalDate.of(2025,6,2),user);
        launch3.setId(3L);
        LaunchDTO launch3DTO = new LaunchDTO(launch3);

        when(launchService.updateLaunch(anyLong(),any(Launch.class),Mockito.eq("fake-token"))).thenReturn(launch3DTO);

        mockMvc.perform(put("/user/launches/update/{id}",3L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(launch3DTO))
                .header("Authorization","fake-token"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("academy"));
    }

    @DisplayName("test delete launch SUCCESS")
    @Test
    void testDeleteLaunchSuccess() throws Exception {

        doNothing().when(launchService).deleteLaunchById(anyLong(),Mockito.eq("fake-token"));

        mockMvc.perform(delete("/user/launches/delete/{id}",2L)
                .header("Authorization","fake-token"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}
