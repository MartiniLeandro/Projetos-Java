package com.money_track.demo.entities.DTO;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.enums.TypeValue;

public record CategoryDTO(Long id, String name, TypeValue typeValue){
    public CategoryDTO(Category category) {
        this(
                category.getId(), category.getName(), category.getTypeValue()
        );
    }
}
