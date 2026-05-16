package com.money_track.demo.entities.DTO;

import com.money_track.demo.entities.enums.TypeValue;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record LaunchesFilterDTO(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate initialDate,@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate finalDate, TypeValue typeValue, Long categoryId) {
}
