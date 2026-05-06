package com.money_track.demo.entities.DTO;

import java.math.BigDecimal;

public record CategoryTotalPorcentagemDTO(String name, Double totalValue, BigDecimal porcentagem) {
    public CategoryTotalPorcentagemDTO{
        if(totalValue == null){
            totalValue = 0.0;
        }
    }
}
