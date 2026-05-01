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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return launchRepository.getTotalRevenue(user.getId());
    }
}
