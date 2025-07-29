package com.money_track.demo.services;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.LaunchDTO;
import com.money_track.demo.entities.Launch;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.Roles;
import com.money_track.demo.entities.enums.TypeValue;
import com.money_track.demo.exceptions.IsNotYoursException;
import com.money_track.demo.exceptions.NegativeNumberException;
import com.money_track.demo.exceptions.NotFoundException;
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
import java.util.ArrayList;
import java.util.Collections;
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

        when(tokenService.validateToken(anyString())).thenReturn("user@email.com");
        when(userRepository.findUserByEmail("user@email.com")).thenReturn(user1);

    }

    @DisplayName("test find all launches SUCCESS")
    @Test
    void testFindAllLaunchesSuccess(){

        List<LaunchDTO> allLaunches = launchService.findAllLaunches("fake-token");

        Assertions.assertNotNull(allLaunches);
        Assertions.assertEquals(2,allLaunches.size());
    }

    @DisplayName("test final all launches FAILED")
    @Test
    void testFindAllLaunchesFailed(){
        when(launchService.findAllLaunches("fake-token")).thenThrow(new RuntimeException("Erro interno"));

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> launchService.findAllLaunches("fake-token"));
        Assertions.assertEquals("Erro interno", exception.getMessage());
    }

    @DisplayName("test find launch by id SUCCESS")
    @Test
    void testFindLaunchByIdSuccess(){
        when(launchRepository.findById(anyLong())).thenReturn(Optional.of(launch1));
        LaunchDTO launch = launchService.findLaunchById("fake-token",1L);

        Assertions.assertEquals(1500.0,launch.getValue());

    }

    @DisplayName("test find launch by id FAILED case1")
    @Test
    void testFindLaunchByIdFailed1(){
        when(launchRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> launchService.findLaunchById("fake-token",anyLong()));

        Assertions.assertEquals("Não existe launch com este ID",exception.getMessage());
    }

    @DisplayName("test find launch by id FAILED case2")
    @Test
    void testFindLaunchByIdFailed2(){
        user1.setLaunches(new ArrayList<>());
        when(launchRepository.findById(1L)).thenReturn(Optional.of(launch1));

        IsNotYoursException exception = Assertions.assertThrows(IsNotYoursException.class, () -> launchService.findLaunchById("fake-token",1L));

        Assertions.assertEquals("este launch não pertence a você",exception.getMessage());

    }

    @DisplayName("test create launch SUCCESS")
    @Test
    void testCreateLaunchSuccess(){
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category1));
        when(launchRepository.save(any())).thenReturn(launch2);
        LaunchDTO launch = launchService.createLaunch(launch2,"fake-token");

        Assertions.assertEquals("school",launch.getDescription());
    }

    @DisplayName("test create launch FAILED case1")
    @Test
    void testCreateLaunchFailed1(){
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> launchService.createLaunch(launch2,"fake-token"));
        Assertions.assertEquals("Não existe category com este ID",exception.getMessage());
    }

    @DisplayName("test create launch FAILED case2")
    @Test
    void testCreateLaunchFailed2(){
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category1));
        launch2.setValue(-50D);

        NegativeNumberException exception = Assertions.assertThrows(NegativeNumberException.class, () ->launchService.createLaunch(launch2,"fake-token"));
        Assertions.assertEquals("Value não pode ser negativo",exception.getMessage());
        verify(launchRepository, never()).save(any(Launch.class));
    }

    @DisplayName("test update launch SUCCESS")
    @Test
    void testUpdateLaunchSuccess(){
        when(launchRepository.findById(anyLong())).thenReturn(Optional.of(launch1));
        when(categoryRepository.existsById(anyLong())).thenReturn(true);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category1));
        when(launchRepository.save(any())).thenReturn(launch1);

        LaunchDTO launchDTO = launchService.updateLaunch(1L,launch2,"fake-token");
        Assertions.assertNotNull(launchDTO);
        Assertions.assertEquals("school", launchDTO.getDescription());

    }

    @DisplayName("test update launch FAILED case1")
    @Test
    void testUpdateLaunchFailed1(){
        when(launchRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> launchService.updateLaunch(1L,launch2,"fake-token"));
        Assertions.assertEquals("Não existe launch com este ID",exception.getMessage());
    }

    @DisplayName("test update launch FAILED case2")
    @Test
    void testUpdateFailed2(){
        user1.setLaunches(new ArrayList<>());
        when(launchRepository.findById(1L)).thenReturn(Optional.of(launch1));

        IsNotYoursException exception = Assertions.assertThrows(IsNotYoursException.class, () -> launchService.updateLaunch(1L,launch1,"fake-token"));
        Assertions.assertEquals("Este launch não pertence a você",exception.getMessage());
    }

    @DisplayName("test update launch FAILED case3")
    @Test
    void testUpdateFailed3(){
        when(launchRepository.findById(anyLong())).thenReturn(Optional.of(launch2));
        when(categoryRepository.existsById(anyLong())).thenReturn(false);

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> launchService.updateLaunch(1L,launch1,"fake-token"));
        Assertions.assertEquals("Não existe esta category",exception.getMessage());
    }

    @DisplayName("test update launch FAILED case4")
    @Test
    void testUpdateFailed4(){
        when(launchRepository.findById(anyLong())).thenReturn(Optional.of(launch2));
        when(categoryRepository.existsById(anyLong())).thenReturn(true);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> launchService.updateLaunch(1L,launch1,"fake-token"));
        Assertions.assertEquals("Não existe category com este ID", exception.getMessage());
    }

    @DisplayName("test update launch FAILED case5")
    @Test
    void testUpdateFailed5(){
        when(launchRepository.findById(anyLong())).thenReturn(Optional.of(launch2));
        launch1.setValue(-50D);
        when(categoryRepository.existsById(anyLong())).thenReturn(true);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category1));

        NegativeNumberException exception = Assertions.assertThrows(NegativeNumberException.class, () -> launchService.updateLaunch(1L,launch1,"fake-token"));
        Assertions.assertEquals("Value não pode ser negativo",exception.getMessage());
    }

    @DisplayName("test delete launch SUCCESS")
    @Test
    void testDeleteLaunchSuccess(){
        when(launchRepository.findById(anyLong())).thenReturn(Optional.of(launch1));
        launchService.deleteLaunchById(1L,"fake-token");

        verify(launchRepository).deleteById(1L);
    }


    @DisplayName("test delete launch FAILED case1")
    @Test
    void testDeleteLaunchFailed1(){
        when(launchRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> launchService.deleteLaunchById(1L,"fake-token"));
        Assertions.assertEquals("Não existe launch com este ID",exception.getMessage());
        verify(launchRepository, never()).deleteById(anyLong());
    }

    @DisplayName("test delete launch FAILED case2")
    @Test
    void testDeleteLaunchFailed2(){
        when(launchRepository.findById(1L)).thenReturn(Optional.of(launch1));
        user1.setLaunches(new ArrayList<>());

        IsNotYoursException exception = Assertions.assertThrows(IsNotYoursException.class, () -> launchService.deleteLaunchById(1L, "fake-token"));
        Assertions.assertEquals("Este launch não pertence a você", exception.getMessage());
        verify(launchRepository, never()).deleteById(anyLong());
    }

    @DisplayName("test filter launch by category SUCCESS")
    @Test
    void testFilterLaunchByCategorySuccess(){
        List<LaunchDTO> launchesByCategory = launchService.filterLaunchByCategory(category1,"fake-token");

        Assertions.assertNotNull(launchesByCategory);
        Assertions.assertEquals(1,launchesByCategory.size());
        Assertions.assertEquals("salary", launchesByCategory.getFirst().getDescription());
    }

    @DisplayName("test filter launch by category FAILED")
    @Test
    void testFilterLaunchByCategoryFailed(){
        user1.setLaunches(new ArrayList<>());
        List<LaunchDTO> launchesByCategory = launchService.filterLaunchByCategory(category1,"fake-token");

        Assertions.assertNotNull(launchesByCategory);
        Assertions.assertTrue(launchesByCategory.isEmpty());
    }

    @DisplayName("test filter launch by date SUCCESS")
    @Test
    void testFilterLaunchByDateSuccess(){
        when(launchRepository.findByUserAndDateBetween(user1,LocalDate.of(2025,5,1), LocalDate.of(2025,6,30))).thenReturn(List.of(launch1,launch2));
        List<LaunchDTO> launchesByDate = launchService.filterLaunchByDate(LocalDate.of(2025,5,1), LocalDate.of(2025,6,30),"fake-token");

        Assertions.assertNotNull(launchesByDate);
        Assertions.assertEquals(2,launchesByDate.size());
    }

    @DisplayName("test filter launch by date FAILED")
    @Test
    void testFilterLaunchByDateFailed(){
        when(launchRepository.findByUserAndDateBetween(user1,LocalDate.of(2025,5,1), LocalDate.of(2025,6,30))).thenReturn(Collections.emptyList());
        List<LaunchDTO> launchesByDate = launchService.filterLaunchByDate(LocalDate.of(2025,5,1), LocalDate.of(2025,6,30),"fake-token");

        Assertions.assertNotNull(launchesByDate);
        Assertions.assertTrue(launchesByDate.isEmpty());
    }

    @DisplayName("test filter by type value SUCCESS")
    @Test
    void testFilterLaunchByTypeValueSuccess(){
        when(launchRepository.findByUserAndCategory_TypeValue(user1,TypeValue.EXPENSE)).thenReturn(List.of(launch2));
        List<LaunchDTO> launchesByTypeValue = launchService.filterByTypeValue("fake-token",TypeValue.EXPENSE);

        Assertions.assertNotNull(launchesByTypeValue);
        Assertions.assertEquals(1,launchesByTypeValue.size());
    }

    @DisplayName("test filter by type value FAILED")
    @Test
    void testFilterLaunchByTypeValueFailed(){
        when(launchRepository.findByUserAndCategory_TypeValue(user1,TypeValue.EXPENSE)).thenReturn(Collections.emptyList());
        List<LaunchDTO> launchesByTypeValue = launchService.filterByTypeValue("fake-token",TypeValue.EXPENSE);

        Assertions.assertNotNull(launchesByTypeValue);
        Assertions.assertTrue(launchesByTypeValue.isEmpty());
    }
}
