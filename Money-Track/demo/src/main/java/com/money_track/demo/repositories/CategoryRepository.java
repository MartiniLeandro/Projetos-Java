package com.money_track.demo.repositories;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.enums.TypeValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByNameAndTypeValue(String name, TypeValue type);
    Category findByName(String name);
    Page<Category> findAll(Pageable pageable);

    @Query(value = "select * from categories where (:type_value is null or type_value = :type_value)", nativeQuery = true)
    List<Category> findAllByTypeValue(String type_value);
}
