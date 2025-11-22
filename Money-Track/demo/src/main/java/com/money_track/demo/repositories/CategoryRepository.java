package com.money_track.demo.repositories;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.enums.TypeValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByNameAndTypeValue(String name, TypeValue type);
    Category findByName(String name);
}
