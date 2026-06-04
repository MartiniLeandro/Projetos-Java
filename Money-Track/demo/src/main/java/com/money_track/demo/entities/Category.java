package com.money_track.demo.entities;

import com.money_track.demo.entities.DTO.CategoryDTO;
import com.money_track.demo.entities.enums.TypeValue;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    private TypeValue typeValue;

    @Column(name = "icon")
    private String icon;

    @Column(name= "color", length = 7)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Category(CategoryDTO categoryDTO) {
        this.name = categoryDTO.name();
        this.typeValue = categoryDTO.typeValue();
        this.icon = categoryDTO.icon();
        this.color = categoryDTO.color();
    }

}
