package com.money_track.demo.services;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.LaunchDTO;
import com.money_track.demo.entities.Launch;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.Roles;
import com.money_track.demo.entities.enums.TypeValue;
import com.money_track.demo.repositories.CategoryRepository;
import com.money_track.demo.repositories.LaunchRepository;
import com.money_track.demo.repositories.UserRepository;
import com.money_track.demo.security.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LaunchServiceTest {

    @Mock
    private LaunchRepository launchRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private LaunchService launchService;

    private Launch launch1,launch2;
    private User user1;
    private Category category1,category2;

    @BeforeEach
    void setup(){
        user1 = new User("user","702.413.770-30","user@email.com","user123", Roles.ROLE_USER);
        category1 = new Category("salary", TypeValue.REVENUE);
        category2 = new Category("school", TypeValue.EXPENSE);
        category1.setId(1L);
        category2.setId(2L);

        launch1 = new Launch("salary",category1,1500.0, LocalDate.of(2025,6,1),user1);
        launch2 = new Launch("school",category2,500.0, LocalDate.of(2025,6,2),user1);
        launch1.setId(1L);
        launch2.setId(2L);
        user1.setLaunches(List.of(launch1,launch2));

    }

    @DisplayName("test find all launches SUCCESS")
    @Test
    void testFindAllLaunchesSuccess(){
        when(tokenService.validateToken(anyString())).thenReturn("user@email.com");
        when(userRepository.findUserByEmail("user@email.com")).thenReturn(user1);
        List<LaunchDTO> allLaunches = launchService.findAllLaunches("fake-token");

        Assertions.assertNotNull(allLaunches);
        Assertions.assertEquals(2,allLaunches.size());
    }

    @DisplayName("test find launch by id SUCCESS")
    @Test
    void testFindLaunchByIdSuccess(){
        when(tokenService.validateToken(anyString())).thenReturn("user@email.com");
        when(userRepository.findUserByEmail("user@email.com")).thenReturn(user1);
        when(launchRepository.findById(anyLong())).thenReturn(Optional.of(launch1));
        LaunchDTO launch = launchService.findLaunchById("fake-token",1L);

        Assertions.assertEquals(1500.0,launch.getValue());

    }

    @DisplayName("test create launch SUCCESS")
    @Test
    void testCreateLaunch(){
        when(tokenService.validateToken(anyString())).thenReturn("user@email.com");
        when(userRepository.findUserByEmail("user@email.com")).thenReturn(user1);
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category1));
        when(launchRepository.save(any())).thenReturn(launch2);
        LaunchDTO launch = launchService.createLaunch(launch2,"fake-token");

        Assertions.assertEquals("school",launch.getDescription());
    }

    @DisplayName("test update launch SUCCESS")
    @Test
    void testUpdateLaunchSuccess(){
        when(tokenService.validateToken(anyString())).thenReturn("user@email.com");
        when(userRepository.findUserByEmail("user@email.com")).thenReturn(user1);
        when(launchRepository.findById(anyLong())).thenReturn(Optional.of(launch1));
        when(categoryRepository.existsById(anyLong())).thenReturn(true);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category1));
        when(launchRepository.save(any())).thenReturn(launch1);

        LaunchDTO launchDTO = launchService.updateLaunch(1L,launch2,"fake-token");
        Assertions.assertNotNull(launchDTO);
        Assertions.assertEquals("school", launchDTO.getDescription());

    }

    @DisplayName("test delete launch SUCCESS")
    @Test
    void testDeleteLaunchSuccess(){
        when(tokenService.validateToken(anyString())).thenReturn("user@email.com");
        when(userRepository.findUserByEmail("user@email.com")).thenReturn(user1);
        when(launchRepository.findById(anyLong())).thenReturn(Optional.of(launch1));
        launchService.deleteLaunchById(1L,"fake-token");

        verify(launchRepository).deleteById(1L);
    }

}
