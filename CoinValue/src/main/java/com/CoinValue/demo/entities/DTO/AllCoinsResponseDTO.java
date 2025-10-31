package com.CoinValue.demo.entities.DTO;

import java.util.List;

public record AllCoinsResponseDTO(Integer count, List<String> currencies) {
}
