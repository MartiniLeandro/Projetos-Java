package com.money_track.demo.entities.DTO;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.Launch;

import java.time.LocalDate;

public class LaunchDTO {
    private final Long id;
    private String description;
    private Category category;
    private Double value;
    private LocalDate date;

    public LaunchDTO(Launch launch){
        this.id = launch.getId();
        this.description = launch.getDescription();
        this.category = launch.getCategory();
        this.value = launch.getValue();
        this.date = launch.getDate();
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
