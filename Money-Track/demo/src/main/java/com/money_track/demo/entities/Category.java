package com.money_track.demo.entities;

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

}
