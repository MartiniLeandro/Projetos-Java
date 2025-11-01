package com.CoinValue.demo.entities.DTO;

import java.util.Map;

public record ConvertAllCoinsDTO(Double amount, String base, Map<String,Double> rates) {
}
