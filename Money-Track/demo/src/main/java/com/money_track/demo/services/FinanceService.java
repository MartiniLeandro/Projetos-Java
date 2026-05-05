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

    public BigDecimal getTotalRevenueByMonth(Integer year, Integer month){
        Long userId = getCurrentUser();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        BigDecimal valorTotal = launchRepository.getTotalRevenueByMonth(userId, startDate, endDate);
        return valorTotal != null ? valorTotal : BigDecimal.ZERO;
    }

    public BigDecimal getTotalExpenseByMonth(Integer year, Integer month){
        Long userId = getCurrentUser();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        BigDecimal valorTotal = launchRepository.getTotalExpenseByMonth(userId, startDate, endDate);
        return valorTotal != null ? valorTotal : BigDecimal.ZERO;
    }

    public BigDecimal getTotalBalanceByMonth(Integer year, Integer month){
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

    public List<CategoryTotalDTO> getTotalRevenueByCategoriesByMonth(Integer year, Integer month){
        Long userId = getCurrentUser();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return launchRepository.getTotalRevenueByCategoriesByMonth(userId, startDate, endDate);
    }

    public List<CategoryTotalDTO> getTotalExpenseByCategoriesByMonth(Integer year, Integer month){
        Long userId = getCurrentUser();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return launchRepository.getTotalExpenseByCategoriesByMonth(userId, startDate, endDate);
    }

    public List<LaunchInterface> getLastLaunches(Integer year, Integer month){
        Long userId = getCurrentUser();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return launchRepository.getLastLaunches(userId, startDate, endDate);
    }

    public DashboardHome getDashboardData(Integer year, Integer month){
        Integer finalYear = (year != null) ? year : 2025; //valor padrão que tem lançamento no banco de dados para o user teste
        Integer finalMonth = (month != null) ? month : 9; //valor padrão que tem lançamento no banco de dados para o user teste
        BigDecimal totalRevenueMonth = getTotalRevenueByMonth(finalYear, finalMonth);
        BigDecimal totalExpenseByMonth = getTotalExpenseByMonth(finalYear, finalMonth);
        BigDecimal totalBalanceByMonth = getTotalBalanceByMonth(finalYear, finalMonth);
        List<CategoryTotalDTO> totalRevenueCategoriesMonth = getTotalRevenueByCategoriesByMonth(finalYear, finalMonth);
        List<CategoryTotalDTO> totalExpenseCategoriesMonth = getTotalExpenseByCategoriesByMonth(finalYear, finalMonth);
        List<LaunchInterface> lastLaunches = getLastLaunches(finalYear, finalMonth);
        return new DashboardHome(totalRevenueMonth,totalExpenseByMonth,totalBalanceByMonth,totalRevenueCategoriesMonth, totalExpenseCategoriesMonth, lastLaunches);
    }

    private Long getCurrentUser(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}
