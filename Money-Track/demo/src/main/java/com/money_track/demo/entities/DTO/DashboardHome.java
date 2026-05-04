package com.money_track.demo.entities.DTO;

import java.math.BigDecimal;
import java.util.List;

public record DashboardHome(
        BigDecimal totalRevenueMonth,
        BigDecimal totalExpenseMonth,
        BigDecimal totalBalanceMonth,
        List<CategoryTotalDTO> totalRevenueCategoriesMonth,
        List<CategoryTotalDTO> totalExpenseCategoriesMonth,
        List<LaunchInterface> lastLaunches
) {}
