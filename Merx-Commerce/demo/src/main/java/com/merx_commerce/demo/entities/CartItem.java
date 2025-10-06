package com.merx_commerce.demo.entities;

import com.merx_commerce.demo.entities.DTOS.CartItemRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cartsItems")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private Integer quantity;

    private BigDecimal price;

    public CartItem(CartItemRequestDTO data){
        this.id = data.id();
        this.product = data.product();
        this.cart = data.cart();
        this.quantity = data.quantity();
        this.price = data.price();
    }
}
