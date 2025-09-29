package com.merx_commerce.demo.entities.DTOS;

import com.merx_commerce.demo.entities.Category;
import com.merx_commerce.demo.entities.Product;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponseDTO(Long id, String name, String description, BigDecimal price, Integer quantityInStock, List<String> images, Category category) {
    public ProductResponseDTO(Product product){
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantityInStock(),
                product.getImages(),
                product.getCategory()
        );
    }
}
