package com.money_track.demo.repositories;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.enums.TypeValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setup(){
        category = new Category("salário", TypeValue.REVENUE);
    }

    @Test
    void createCategory(){
        categoryRepository.save(category);

        assertTrue(categoryRepository.findById(category.getId()).isPresent());
    }
}

