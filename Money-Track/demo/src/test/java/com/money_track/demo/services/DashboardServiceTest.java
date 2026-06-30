package com.money_track.demo.services;

import com.money_track.demo.entities.DTO.CategoryTotalDTO;
import com.money_track.demo.entities.DTO.CategoryTotalPorcentagemDTO;
import com.money_track.demo.entities.DTO.DashboardHome;
import com.money_track.demo.entities.DTO.LaunchInterface;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.Roles;
import com.money_track.demo.repositories.LaunchRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceTest {

    @Mock
    private LaunchRepository launchRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private DashboardService dashboardService;

    private User user;

    @BeforeEach
    void setup(){
        user = new User(1L,"user","702.413.770-30","user@email.com","user123", Roles.ROLE_USER, new ArrayList<>());

        when(authentication.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @DisplayName("test get total revenue by categories and month SUCCESS")
    @Test
    void testGetTotalRevenueByCategoriesByMonthSuccess() {
        CategoryTotalDTO category1 = new CategoryTotalDTO("salary", 1500.0);
        CategoryTotalDTO category2 = new CategoryTotalDTO("freelancer", 350.0);
        when(launchRepository.getTotalRevenueByCategoriesByMonth(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(List.of(category1, category2));
        List<CategoryTotalPorcentagemDTO> categoriesTotal = dashboardService.getTotalRevenueByCategoriesByMonth(2026, 6,BigDecimal.valueOf(1850));

        Assertions.assertNotNull(categoriesTotal);
        Assertions.assertEquals(2, categoriesTotal.size());
        Assertions.assertEquals(category1.totalValue(), categoriesTotal.getFirst().totalValue());
        Assertions.assertEquals("salary", categoriesTotal.get(0).name());
        Assertions.assertEquals("freelancer", categoriesTotal.get(1).name());
        Assertions.assertEquals(new BigDecimal("81.0800"), categoriesTotal.get(0).porcentagem());
        Assertions.assertEquals(new BigDecimal("18.9200"), categoriesTotal.get(1).porcentagem());
    }

    @DisplayName("test get total revenue by categories EMPTY")
    @Test
    void testGetTotalRevenueByCategoriesByMonthEmpty(){
        when(launchRepository.getTotalRevenueByCategoriesByMonth(anyLong(), any(), any())).thenReturn(Collections.emptyList());
        List<CategoryTotalPorcentagemDTO> categoriesTotal = dashboardService.getTotalRevenueByCategoriesByMonth(2026, 6, BigDecimal.ZERO);

        Assertions.assertNotNull(categoriesTotal);
        Assertions.assertTrue(categoriesTotal.isEmpty());
    }

    @DisplayName("test get total revenue by categories EDGE CASE")
    @Test
    void testGetTotalRevenueByCategoriesByMonthZeroTotal(){
        CategoryTotalDTO category1 = new CategoryTotalDTO("salary", 0.0);
        when(launchRepository.getTotalRevenueByCategoriesByMonth(anyLong(), any(), any())).thenReturn(List.of(category1));
        List<CategoryTotalPorcentagemDTO> categoriesTotal = dashboardService.getTotalRevenueByCategoriesByMonth(2026, 6, BigDecimal.ZERO);

        Assertions.assertNotNull(categoriesTotal);
        Assertions.assertEquals(BigDecimal.ZERO, categoriesTotal.getFirst().porcentagem());
    }

    @DisplayName("test get total expense by categories and month SUCCESS")
    @Test
    void testGetTotalExpenseByCategoriesByMonthSuccess() {
        CategoryTotalDTO category1 = new CategoryTotalDTO("school", 1500.0);
        CategoryTotalDTO category2 = new CategoryTotalDTO("academy", 350.0);
        when(launchRepository.getTotalExpenseByCategoriesByMonth(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(List.of(category1, category2));
        List<CategoryTotalPorcentagemDTO> categoriesTotal = dashboardService.getTotalExpenseByCategoriesByMonth(2026, 6, BigDecimal.valueOf(1850));

        Assertions.assertNotNull(categoriesTotal);
        Assertions.assertEquals(2, categoriesTotal.size());
        Assertions.assertEquals(category1.totalValue(), categoriesTotal.getFirst().totalValue());
        Assertions.assertEquals("school", categoriesTotal.get(0).name());
        Assertions.assertEquals("academy", categoriesTotal.get(1).name());
        Assertions.assertEquals(new BigDecimal("81.0800"), categoriesTotal.get(0).porcentagem());
        Assertions.assertEquals(new BigDecimal("18.9200"), categoriesTotal.get(1).porcentagem());
    }

    @DisplayName("test get total expense by categories EMPTY")
    @Test
    void testGetTotalExpenseByCategoriesByMonthEmpty(){
        when(launchRepository.getTotalExpenseByCategoriesByMonth(anyLong(), any(), any())).thenReturn(Collections.emptyList());
        List<CategoryTotalPorcentagemDTO> categoriesTotal = dashboardService.getTotalExpenseByCategoriesByMonth(2026, 6, BigDecimal.ZERO);

        Assertions.assertNotNull(categoriesTotal);
        Assertions.assertTrue(categoriesTotal.isEmpty());
    }

    @DisplayName("test get total expense by categories EDGE CASE")
    @Test
    void testGetTotalExpenseByCategoriesByMonthZeroTotal(){
        CategoryTotalDTO category1 = new CategoryTotalDTO("salary", 0.0);
        when(launchRepository.getTotalExpenseByCategoriesByMonth(anyLong(), any(), any())).thenReturn(List.of(category1));
        List<CategoryTotalPorcentagemDTO> categoriesTotal = dashboardService.getTotalExpenseByCategoriesByMonth(2026, 6, BigDecimal.ZERO);

        Assertions.assertNotNull(categoriesTotal);
        Assertions.assertEquals(BigDecimal.ZERO, categoriesTotal.getFirst().porcentagem());
    }

    @DisplayName("test get dashboard data SUCCESS")
    @Test
    void testGetDashboardDataSuccess(){
        CategoryTotalDTO categoryRevenue1 = new CategoryTotalDTO("salary", 1500.0);
        CategoryTotalDTO categoryRevenue2 = new CategoryTotalDTO("freelancer", 500.0);
        CategoryTotalDTO categoryExpense1 = new CategoryTotalDTO("school", 1200.0);
        CategoryTotalDTO categoryExpense2 = new CategoryTotalDTO("academy", 300.0);

        when(launchRepository.getTotalRevenueByMonth(anyLong(), any(), any())).thenReturn(BigDecimal.valueOf(2000));
        when(launchRepository.getTotalExpenseByMonth(anyLong(), any(), any())).thenReturn(BigDecimal.valueOf(1500));
        when(launchRepository.getTotalRevenueByCategoriesByMonth(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(List.of(categoryRevenue1, categoryRevenue2));
        when(launchRepository.getTotalExpenseByMonth(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(BigDecimal.valueOf(1500));
        when(launchRepository.getTotalExpenseByCategoriesByMonth(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(List.of(categoryExpense1, categoryExpense2));
        when(launchRepository.getLastLaunches(anyLong(), any(), any())).thenReturn(List.of());
        DashboardHome dashboardData = dashboardService.getDashboardData(2026,6);

        Assertions.assertNotNull(dashboardData);
        Assertions.assertEquals(BigDecimal.valueOf(2000),dashboardData.totalRevenueMonth());
        Assertions.assertEquals(BigDecimal.valueOf(1500),dashboardData.totalExpenseMonth());
        Assertions.assertEquals(BigDecimal.valueOf(500),dashboardData.totalBalanceMonth());
        Assertions.assertEquals(2,dashboardData.totalExpenseCategoriesMonth().size());
        Assertions.assertEquals(2,dashboardData.totalRevenueCategoriesMonth().size());
        Assertions.assertEquals(0,dashboardData.lastLaunches().size());
    }
}
