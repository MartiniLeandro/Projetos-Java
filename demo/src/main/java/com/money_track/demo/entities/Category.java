package com.money_track.demo.entities;

import com.money_track.demo.entities.enums.TypeValue;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "Category")
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    private TypeValue typeValue;

    public Category(){}
    public Category(String name, TypeValue typeValue) {
        this.name = name;
        this.typeValue = typeValue;
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
