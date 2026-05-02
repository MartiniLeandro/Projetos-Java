package com.money_track.demo.entities.DTO;

import java.math.BigDecimal;

public record CategoryTotalDTO(String name, Double totalValue) {
    public CategoryTotalDTO{
        if(totalValue == null){
            totalValue = 0.0;
        }
    }
}
