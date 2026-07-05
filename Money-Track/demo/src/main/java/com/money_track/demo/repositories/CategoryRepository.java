package com.money_track.demo.repositories;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.enums.TypeValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByNameAndTypeValueAndUserId(String name, TypeValue type, Long userId);

    @Query(value = "select * from categories where (:type_value is null or type_value = :type_value) and (user_id is null or user_id = :userId) order by name asc", nativeQuery = true)
    List<Category> findAllByTypeValueAndUserId(@Param("type_value") String type_value, @Param("userId") Long userId);

    @Query(value = "select * from categories where (user_id is null or user_id = :userId) and (:type_value is null or type_value= :type_value) and (:name is null or name ilike concat('%', :name, '%')) order by name asc", nativeQuery = true)
    List<Category> findGlobalAndUserCategories(@Param("userId") Long userId, @Param("name") String name, @Param("type_value") String type_value);
}
