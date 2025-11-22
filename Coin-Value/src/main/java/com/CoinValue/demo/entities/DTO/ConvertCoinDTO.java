package com.CoinValue.demo.entities.DTO;

import lombok.Builder;

@Builder
public record ConvertCoinDTO(Double amount, String from, String to, Double result) {
}
