package com.money_track.demo.services;

import com.money_track.demo.entities.User;
import com.money_track.demo.repositories.LaunchRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

        totalRevenue = totalRevenue != null ? totalRevenue : BigDecimal.ZERO;
        totalExpense = totalExpense != null ? totalExpense : BigDecimal.ZERO;

        return totalRevenue.subtract(totalExpense);
    }

    private Long getCurrentUser(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}
