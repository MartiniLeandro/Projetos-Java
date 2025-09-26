package com.merx_commerce.demo.repositories;

import com.merx_commerce.demo.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
