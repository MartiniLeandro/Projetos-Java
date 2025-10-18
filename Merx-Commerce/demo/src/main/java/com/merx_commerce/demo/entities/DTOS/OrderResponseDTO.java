package com.merx_commerce.demo.entities.DTOS;

import com.merx_commerce.demo.entities.Enums.StatusOrder;
import com.merx_commerce.demo.entities.Order;
import com.merx_commerce.demo.entities.OrderItems;
import com.merx_commerce.demo.entities.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(Long id,User user, List<OrderItems> items, BigDecimal total, StatusOrder status, LocalDateTime date) {
    public OrderResponseDTO(Order order){
        this (
                order.getId(),order.getUser(),order.getItems(),order.getTotal(),order.getStatus(),order.getDate()
        );
    }
}
