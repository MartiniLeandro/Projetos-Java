package com.merx_commerce.demo.repositories;

import com.merx_commerce.demo.entities.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems,Long> {
}
