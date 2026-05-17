package com.money_track.demo.entities.DTO;

import java.util.List;

public record LaunchesDataDTO(List<LaunchDTO> launches, TypeValuesDTO typeValues, int totalLaunches, Double totalValue, List<CategoryTotalDTO> categoryTotals) {
}
