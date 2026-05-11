package com.money_track.demo.services;

import com.money_track.demo.entities.DTO.*;
import com.money_track.demo.entities.User;
import com.money_track.demo.repositories.LaunchRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public List<CategoryTotalDTO> getTotalRevenueByCategories(){ //adicionar depois com a porcentagem
        return launchRepository.getTotalRevenueByCategories(getCurrentUser());
    }

    public List<CategoryTotalDTO> getTotalExpenseByCategories(){ //adicionar depois com a porcentagem
        return launchRepository.getTotalExpenseByCategories(getCurrentUser());
    }

    public List<CategoryTotalPorcentagemDTO> getTotalRevenueByCategoriesByMonth(Integer year, Integer month){
        Long userId = getCurrentUser();
        BigDecimal total = getTotalRevenueByMonth(year, month);
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        List<CategoryTotalDTO> categories = launchRepository.getTotalRevenueByCategoriesByMonth(userId, startDate, endDate);
        return getCategoriesWithPorcentagem(categories, total);
    }

    public List<CategoryTotalPorcentagemDTO> getTotalExpenseByCategoriesByMonth(Integer year, Integer month){
        Long userId = getCurrentUser();
        BigDecimal total = getTotalExpenseByMonth(year, month);
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        List<CategoryTotalDTO> categories = launchRepository.getTotalExpenseByCategoriesByMonth(userId, startDate, endDate);
        return getCategoriesWithPorcentagem(categories, total);
    }

    public List<LaunchInterface> getLastLaunches(Integer year, Integer month){
        Long userId = getCurrentUser();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return launchRepository.getLastLaunches(userId, startDate, endDate);
    }

    public DashboardHome getDashboardData(Integer year, Integer month){
        Integer finalYear = (year != null) ? year : LocalDate.now().getYear();
        Integer finalMonth = (month != null) ? month : LocalDate.now().getMonthValue();
        BigDecimal totalRevenueMonth = getTotalRevenueByMonth(finalYear, finalMonth);
        BigDecimal totalExpenseByMonth = getTotalExpenseByMonth(finalYear, finalMonth);
        BigDecimal totalBalanceByMonth = getTotalBalanceByMonth(finalYear, finalMonth);
        List<CategoryTotalPorcentagemDTO> totalRevenueCategoriesMonth = getTotalRevenueByCategoriesByMonth(finalYear, finalMonth);
        List<CategoryTotalPorcentagemDTO> totalExpenseCategoriesMonth = getTotalExpenseByCategoriesByMonth(finalYear, finalMonth);
        List<LaunchInterface> lastLaunches = getLastLaunches(finalYear, finalMonth);
        return new DashboardHome(totalRevenueMonth,totalExpenseByMonth,totalBalanceByMonth,totalRevenueCategoriesMonth, totalExpenseCategoriesMonth, lastLaunches);
    }

    private Long getCurrentUser(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

    private List<CategoryTotalPorcentagemDTO> getCategoriesWithPorcentagem(List<CategoryTotalDTO> categories, BigDecimal total){
        return categories.stream().map(categoryTotal -> {
            BigDecimal valorCategoria = new BigDecimal(categoryTotal.totalValue());
            BigDecimal porcentagem = BigDecimal.ZERO;
            if(total.compareTo(BigDecimal.ZERO) > 0){
                porcentagem = valorCategoria.divide(total,4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            }
            return new CategoryTotalPorcentagemDTO(categoryTotal.name(),categoryTotal.totalValue(),porcentagem);
        }).toList();
    }
}
