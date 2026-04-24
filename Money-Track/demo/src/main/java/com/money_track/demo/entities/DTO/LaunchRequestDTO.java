package com.money_track.demo.entities.DTO;

import java.time.LocalDate;

public record LaunchRequestDTO(String description, Long categoryId, Double value, LocalDate date) {
}
