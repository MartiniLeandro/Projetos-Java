package com.money_track.demo.entities.DTO;

import java.math.BigDecimal;
import java.util.List;

public record LaunchesDataDTO(List<LaunchDTO> launches, TypeValuesDTO typeValues, int totalLaunches, BigDecimal totalValue, List<CategoryTotalDTO> categoryTotals) {
}
