package com.money_track.demo.entities.DTO;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.Launch;

import java.time.LocalDate;

public record LaunchDTO(Long id, String description, Category category, Double value, LocalDate date){

    public LaunchDTO(Launch launch) {
        this(
                launch.getId(), launch.getDescription(), launch.getCategory(), launch.getValue(), launch.getDate()
        );
    }
}
