package com.merx_commerce.demo.repositories;

import com.merx_commerce.demo.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
