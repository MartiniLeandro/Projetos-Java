package com.money_track.demo.entities.DTO;

import com.money_track.demo.entities.enums.TypeValue;

import java.time.LocalDate;

public record LaunchesFilterDTO(LocalDate initialDate, LocalDate finalDate, TypeValue typeValue, Long categoryId) {
}
