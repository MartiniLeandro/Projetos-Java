package com.money_track.demo.services;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
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
    private CategoryRepository categoryRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LaunchService launchService;

    private Launch launch1,launch2;
    private User user1;
    private Category category1,category2;
    private LaunchRequestDTO launchRequestDTO;

    @BeforeEach
    void setup(){
        user1 = new User(1L,"user","702.413.770-30","user@email.com","user123", Roles.ROLE_USER, new ArrayList<>());
        category1 = new Category(1L,"salary", TypeValue.REVENUE,"salary","green",user1);
        category2 = new Category(2L,"school", TypeValue.EXPENSE,"school","red",user1);

        launch1 = new Launch(1L,"salary",category1,1500.0, LocalDate.of(2025,6,1),user1);
        launch2 = new Launch(2L,"school",category2,500.0, LocalDate.of(2025,6,2),user1);
        user1.setLaunches(List.of(launch1,launch2));
        launchRequestDTO = new LaunchRequestDTO("recebendo salário",1L,1520.0,LocalDate.now());

        when(authentication.getPrincipal()).thenReturn(user1);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @DisplayName("test find all launches SUCCESS")
    @Test
    void testFindAllLaunchesSuccess(){
        List<Launch> launches = List.of(launch1,launch2);
        Page<Launch> launchesPage = new PageImpl<>(launches);

        when(launchRepository.findAllLaunchesByUser(any(Pageable.class),any(User.class))).thenReturn(launchesPage);
        Page<LaunchDTO> allLaunches = launchService.findAllLaunches(0,2);

        Assertions.assertNotNull(allLaunches);
        Assertions.assertEquals(2,allLaunches.getTotalElements());
        List<LaunchDTO> conteudoDaPagina = allLaunches.getContent();
        Assertions.assertEquals("salary", conteudoDaPagina.getFirst().description());
    }

    @DisplayName("test find all launches FAILED")
    @Test
    void testFindAllLaunchesFailed(){
        when(launchRepository.findAllLaunchesByUser(any(Pageable.class),any(User.class))).thenReturn(Page.empty());
        Page<LaunchDTO> launches = launchService.findAllLaunches(0,2);

        Assertions.assertNotNull(launches);
        Assertions.assertTrue(launches.isEmpty());
    }

    @DisplayName("test find launch by id SUCCESS")
    @Test
    void testFindLaunchByIdSuccess(){
        when(launchRepository.findById(anyLong())).thenReturn(Optional.of(launch1));
        LaunchDTO launch = launchService.findLaunchById(1L);

        Assertions.assertNotNull(launch);
        Assertions.assertEquals("salary", launch.description());
    }

    @DisplayName("test find launch by id FAILED case1")
    @Test
    void testFindLaunchByIdFailed1(){
        when(launchRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> launchService.findLaunchById(1L));

        Assertions.assertEquals("Não existe launch com este ID",exception.getMessage());
    }

    @DisplayName("test find launch by id FAILED case2")
    @Test
    void testFindLaunchByIdFailed2(){
        user1.setLaunches(new ArrayList<>());
        when(launchRepository.findById(1L)).thenReturn(Optional.of(launch1));

        IsNotYoursException exception = Assertions.assertThrows(IsNotYoursException.class, () -> launchService.findLaunchById(1L));

        Assertions.assertEquals("este launch não pertence a você",exception.getMessage());

    }

    @DisplayName("test create launch SUCCESS")
    @Test
    void testCreateLaunchSuccess(){
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category1));
        when(launchRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        LaunchDTO launch = launchService.createLaunch(launchRequestDTO);

        Assertions.assertNotNull(launch);
        Assertions.assertEquals("recebendo salário",launch.description());
    }

    @DisplayName("test create launch FAILED 1")
    @Test
    void testCreateLaunchFailed1(){
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> launchService.createLaunch(launchRequestDTO));

        Assertions.assertEquals("Não existe category com este ID",exception.getMessage());
    }

    @DisplayName("test create launch FAILED 2")
    @Test
    void testCreateLaunchFailed2(){
        LaunchRequestDTO dtoInvalido = new LaunchRequestDTO("teste",2L,-320.0,LocalDate.now());
        NegativeNumberException exception = Assertions.assertThrows(NegativeNumberException.class, () ->launchService.createLaunch(dtoInvalido));

        Assertions.assertEquals("Value não pode ser negativo",exception.getMessage());
        verify(launchRepository, never()).save(any(Launch.class));
    }

    @DisplayName("test update launch SUCCESS")
    @Test
    void testUpdateLaunchSuccess(){
        when(launchRepository.findById(anyLong())).thenReturn(Optional.of(launch1));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category1));
        when(launchRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        LaunchDTO launchDTO = launchService.updateLaunch(1L,launchRequestDTO);

        Assertions.assertNotNull(launchDTO);
        Assertions.assertEquals("recebendo salário", launchDTO.description());

    }

    @DisplayName("test update launch FAILED 1")
    @Test
    void testUpdateFailed1(){
        LaunchRequestDTO dtoInvalido = new LaunchRequestDTO("teste",2L,-320.0,LocalDate.now());

        NegativeNumberException exception = Assertions.assertThrows(NegativeNumberException.class, () -> launchService.updateLaunch(1L,dtoInvalido));
        Assertions.assertEquals("Value não pode ser negativo",exception.getMessage());
    }

    @DisplayName("test update launch FAILED 2")
    @Test
    void testUpdateLaunchFailed2(){
        when(launchRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> launchService.updateLaunch(1L,launchRequestDTO));
        Assertions.assertEquals("Não existe launch com este ID",exception.getMessage());
    }

   @DisplayName("test update launch FAILED 3")
    @Test
    void testUpdateFailed3(){
        User user2 = new User(2L,"user2","702.413.770-30","user2@email.com","user123", Roles.ROLE_USER, new ArrayList<>());
        launch1.setUser(user2);
        when(launchRepository.findById(1L)).thenReturn(Optional.of(launch1));

        IsNotYoursException exception = Assertions.assertThrows(IsNotYoursException.class, () -> launchService.updateLaunch(1L,launchRequestDTO));
        Assertions.assertEquals("Este launch não pertence a você",exception.getMessage());
    }

    @DisplayName("test update launch FAILED 4")
    @Test
    void testUpdateFailed4(){
        when(launchRepository.findById(anyLong())).thenReturn(Optional.of(launch2));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> launchService.updateLaunch(1L,launchRequestDTO));
        Assertions.assertEquals("Não existe category com este ID",exception.getMessage());
    }

    @DisplayName("test delete launch SUCCESS")
    @Test
    void testDeleteLaunchSuccess(){
        when(launchRepository.findById(anyLong())).thenReturn(Optional.of(launch1));
        doNothing().when(launchRepository).delete(launch1);
        launchService.deleteLaunchById(1L);

        verify(launchRepository).delete(launch1);
    }


    @DisplayName("test delete launch FAILED 1")
    @Test
    void testDeleteLaunchFailed1(){
        when(launchRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> launchService.deleteLaunchById(1L));
        Assertions.assertEquals("Não existe launch com este ID",exception.getMessage());
        verify(launchRepository, never()).deleteById(anyLong());
    }

    @DisplayName("test delete launch FAILED 2")
    @Test
    void testDeleteLaunchFailed2(){
        user1.setLaunches(new ArrayList<>());
        User user = new User();
        user.setId(99L);
        launch1.setUser(user);
        when(launchRepository.findById(anyLong())).thenReturn(Optional.of(launch1));

        IsNotYoursException exception = Assertions.assertThrows(IsNotYoursException.class, () -> launchService.deleteLaunchById(1L));
        Assertions.assertEquals("Este launch não pertence a você", exception.getMessage());
        verify(launchRepository, never()).deleteById(anyLong());
    }

    @DisplayName("test filter launches by filters SUCCESS")
    @Test
    void testFilterLaunchByCategorySuccess(){
        LaunchesFilterDTO data = new LaunchesFilterDTO(LocalDate.now(),LocalDate.now(),TypeValue.REVENUE,1L,"teste");
        when(launchRepository.getLaunchesWithFilters(anyLong(),anyString(),anyLong(),any(LocalDate.class),any(LocalDate.class),anyString())).thenReturn(List.of(launch1,launch2));
        List<LaunchDTO> launchesByFilter = launchService.getLaunchesWithFilter(data);

        Assertions.assertNotNull(launchesByFilter);
        Assertions.assertEquals(2,launchesByFilter.size());
        Assertions.assertEquals("salary", launchesByFilter.getFirst().description());
    }

    @DisplayName("test filter launch by filters FAILED")
    @Test
    void testFilterLaunchByCategoryFailed(){
        user1.setLaunches(new ArrayList<>());
        LaunchesFilterDTO data = new LaunchesFilterDTO(LocalDate.now(),LocalDate.now(),TypeValue.REVENUE,1L,"teste");
        when(launchRepository.getLaunchesWithFilters(anyLong(),anyString(),anyLong(),any(LocalDate.class),any(LocalDate.class),anyString())).thenReturn(Collections.emptyList());
        List<LaunchDTO> launchesByCategory = launchService.getLaunchesWithFilter(data);

        Assertions.assertNotNull(launchesByCategory);
        Assertions.assertTrue(launchesByCategory.isEmpty());
    }

    @DisplayName("Test to obtain the total value of transactions by typeValue SUCCESS")
    @Test
    void testGetTypeValuesByLaunchesSuccess(){
        List<LaunchDTO> launches = List.of(launch1,launch2).stream().map(LaunchDTO::new).toList();
        TypeValuesDTO data = launchService.getTypeValuesByLaunches(launches);

        Assertions.assertNotNull(data);
        Assertions.assertEquals(1500.0,data.revenue());
        Assertions.assertEquals(500.0,data.expense());
    }

    @DisplayName("get total category by date SUCCESS")
    @Test
    void testGetTotalCategoryByDateSuccess(){
        CategoryTotalDTO categoryTotalDTO1 = new CategoryTotalDTO("salary",1500.0);
        CategoryTotalDTO categoryTotalDTO2 = new CategoryTotalDTO("school",500.0);
        when(launchRepository.getTotalMostExpensiveCategoriesByDate(anyLong(),any(LocalDate.class),any(LocalDate.class))).thenReturn(List.of(categoryTotalDTO1,categoryTotalDTO2));
        List<CategoryTotalDTO> categoriesTotal = launchService.getCategoryTotalByDate(LocalDate.now().withDayOfMonth(1), LocalDate.now());

        Assertions.assertNotNull(categoriesTotal);
        Assertions.assertEquals(2,categoriesTotal.size());
    }

    @DisplayName("test get total value by all launches")
    @Test
    void testGetTotalValueByAllLaunchesSuccess(){
        when(launchRepository.getTotalRevenue(anyLong())).thenReturn(BigDecimal.valueOf(1500));
        when(launchRepository.getTotalExpense(anyLong())).thenReturn(BigDecimal.valueOf(500));
        BigDecimal value = launchService.getTotalByAllLaunches();

        Assertions.assertNotNull(value);
        Assertions.assertEquals(1000,value.doubleValue());
        }

    @DisplayName("test get launches data SUCCESS")
    @Test
    void testGetLaunchesDataSuccess(){
        LaunchesFilterDTO filters = new LaunchesFilterDTO(LocalDate.now(),LocalDate.now(),TypeValue.REVENUE,1L,"teste");
        when(launchRepository.getLaunchesWithFilters(anyLong(), any(), anyLong(), any(LocalDate.class), any(LocalDate.class), anyString())).thenReturn(List.of(launch1));
        when(launchRepository.getTotalMostExpensiveCategoriesByDate( anyLong(),any(LocalDate.class), any(LocalDate.class))).thenReturn(List.of(new CategoryTotalDTO("salary", 1500.0)));
        when(launchRepository.getTotalRevenue(anyLong())).thenReturn(new BigDecimal("1500.00"));
        when(launchRepository.getTotalExpense(anyLong())).thenReturn(BigDecimal.ZERO);
        LaunchesDataDTO data = launchService.getLaunchesData(filters);

        Assertions.assertNotNull(data);

        Assertions.assertEquals(1, data.launches().size());
        Assertions.assertEquals("salary", data.launches().getFirst().description()); // Ajuste se seu record usar outro nome
        Assertions.assertEquals(1, data.totalLaunches());
        Assertions.assertEquals(new BigDecimal("1500.00"), data.totalValue());
    }

}
