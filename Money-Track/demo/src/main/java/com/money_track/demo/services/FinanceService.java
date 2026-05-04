package com.money_track.demo.services;

import com.money_track.demo.entities.DTO.CategoryTotalDTO;
import com.money_track.demo.entities.DTO.DashboardHome;
import com.money_track.demo.entities.DTO.LaunchDTO;
import com.money_track.demo.entities.DTO.LaunchInterface;
import com.money_track.demo.entities.User;
import com.money_track.demo.repositories.LaunchRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class FinanceService {

    private final LaunchRepository launchRepository;

    public FinanceService(LaunchRepository launchRepository) {
        this.launchRepository = launchRepository;
    }

    public BigDecimal getTotalRevenue(){
        BigDecimal total =  launchRepository.getTotalRevenue(getCurrentUser());
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getTotalExpense(){
        BigDecimal total = launchRepository.getTotalExpense(getCurrentUser());
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getTotalBalance(){
        BigDecimal totalRevenue = getTotalRevenue();
        BigDecimal totalExpense = getTotalExpense();
        return totalRevenue.subtract(totalExpense);
    }

    public BigDecimal getTotalRevenueByMonth(int year, int month){
        Long userId = getCurrentUser();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        BigDecimal valorTotal = launchRepository.getTotalRevenueByMonth(userId, startDate, endDate);
        return valorTotal != null ? valorTotal : BigDecimal.ZERO;
    }

    public BigDecimal getTotalExpenseByMonth(int year, int month){
        Long userId = getCurrentUser();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        BigDecimal valorTotal = launchRepository.getTotalExpenseByMonth(userId, startDate, endDate);
        return valorTotal != null ? valorTotal : BigDecimal.ZERO;
    }

    public BigDecimal getTotalBalanceByMonth(int year, int month){
        BigDecimal totalRevenue = getTotalRevenueByMonth(year, month);
        BigDecimal totalExpense = getTotalExpenseByMonth(year, month);
        return totalRevenue.subtract(totalExpense);
    }

    public List<CategoryTotalDTO> getTotalRevenueByCategories(){
        return launchRepository.getTotalRevenueByCategories(getCurrentUser());
    }

    public List<CategoryTotalDTO> getTotalExpenseBCategories(){
        return launchRepository.getTotalExpenseByCategories(getCurrentUser());
    }

    public List<CategoryTotalDTO> getTotalRevenueByCategoriesByMonth(int year, int month){
        Long userId = getCurrentUser();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return launchRepository.getTotalRevenueByCategoriesByMonth(userId, startDate, endDate);
    }

    public List<CategoryTotalDTO> getTotalExpenseByCategoriesByMonth(int year, int month){
        Long userId = getCurrentUser();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return launchRepository.getTotalExpenseByCategoriesByMonth(userId, startDate, endDate);
    }

    public List<LaunchInterface> getLastLaunches(){
        return launchRepository.getLastLaunches(getCurrentUser());
    }

    public DashboardHome getDashboardData(){
        BigDecimal totalRevenueMonth = getTotalRevenueByMonth(2025,9);
        BigDecimal totalExpenseByMonth = getTotalExpenseByMonth(2025,9);
        BigDecimal totalBalanceByMonth = getTotalBalanceByMonth(2025,9);
        List<CategoryTotalDTO> totalRevenueCategoriesMonth = getTotalRevenueByCategoriesByMonth(2025,9);
        List<CategoryTotalDTO> totalExpenseCategoriesMonth = getTotalExpenseByCategoriesByMonth(2025,9);
        List<LaunchInterface> lastLaunches = getLastLaunches();
        return new DashboardHome(totalRevenueMonth,totalExpenseByMonth,totalBalanceByMonth,totalRevenueCategoriesMonth, totalExpenseCategoriesMonth, lastLaunches);
    }

    private Long getCurrentUser(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}
