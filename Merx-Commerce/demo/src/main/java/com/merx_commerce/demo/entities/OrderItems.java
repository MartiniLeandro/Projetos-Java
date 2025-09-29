package com.merx_commerce.demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "quantity cannot be null")
    private Integer quantity;

    @NotNull(message = "subTotal cannot be null")
    private BigDecimal subTotal;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @NotNull(message = "Product cannot be null")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @NotNull(message = "Order cannot be null")
    private Order order;
}
