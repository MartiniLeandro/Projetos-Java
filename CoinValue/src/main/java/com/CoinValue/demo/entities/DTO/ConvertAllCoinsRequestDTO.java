package com.CoinValue.demo.entities.DTO;

import lombok.Builder;

@Builder
public record ConvertAllCoinsRequestDTO(Double amount, String base) {
}
