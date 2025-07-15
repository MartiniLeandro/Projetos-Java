package com.money_track.demo.entities.DTO;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.enums.TypeValue;

public class CategoryDTO {
    private final Long id;
    private String name;
    private TypeValue typeValue;

    public CategoryDTO(Category category){
        this.id = category.getId();
        this.name = category.getName();
        this.typeValue = category.getTypeValue();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeValue getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(TypeValue typeValue) {
        this.typeValue = typeValue;
    }
}
