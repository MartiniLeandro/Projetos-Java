package com.money_track.demo.entities.DTO;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.Launch;

import java.time.LocalDate;

public record LaunchDTO(Long id, String description, CategoryDTO category, Double value, LocalDate date){

    public LaunchDTO(Launch launch) {
        this(
                launch.getId(), launch.getDescription(), new CategoryDTO(launch.getCategory()), launch.getValue(), launch.getDate()
        );
    }
}
