package com.merx_commerce.demo.entities;

import com.merx_commerce.demo.entities.DTOS.CartRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> items;

    public Cart(CartRequestDTO cart){
        this.id = cart.id();
        this.user = cart.user();
        this.items = cart.items();
    }
}
