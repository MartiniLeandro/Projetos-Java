package com.merx_commerce.demo.entities;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name cannot be blank.")
    private String name;

    @NotBlank(message = "Description cannot be blank.")
    private String description;

    @NotNull(message = "Price is required.")
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required.")
    private Integer quantityInStock;

    @ElementCollection
    @NotNull(message = "Images list cannot be null.")
    private List<String> images;
}