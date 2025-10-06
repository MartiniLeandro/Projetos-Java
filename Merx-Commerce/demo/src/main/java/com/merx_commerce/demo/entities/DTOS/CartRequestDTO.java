package com.merx_commerce.demo.entities.DTOS;

import com.merx_commerce.demo.entities.CartItem;
import com.merx_commerce.demo.entities.User;

import java.util.List;

public record CartRequestDTO(Long id, User user, List<CartItem> items) {
}
