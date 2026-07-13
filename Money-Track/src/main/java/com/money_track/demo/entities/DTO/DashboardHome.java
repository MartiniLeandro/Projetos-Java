package com.money_track.demo.entities.DTO;

import java.math.BigDecimal;
import java.util.List;

public record DashboardHome(
        BigDecimal totalRevenueMonth,
        BigDecimal totalExpenseMonth,
        BigDecimal totalBalanceMonth,
        List<CategoryTotalPorcentagemDTO> totalRevenueCategoriesMonth,
        List<CategoryTotalPorcentagemDTO> totalExpenseCategoriesMonth,
        List<LaunchInterface> lastLaunches
) {}
