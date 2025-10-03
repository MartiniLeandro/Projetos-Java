package com.merx_commerce.demo.services;

import com.merx_commerce.demo.repositories.CartItemsRepository;
import com.merx_commerce.demo.repositories.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;

    public CartService(CartRepository cartRepository, CartItemsRepository cartItemsRepository) {
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
    }
}
