package com.money_track.demo.services;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.CategoryDTO;
import com.money_track.demo.entities.enums.TypeValue;
import com.money_track.demo.exceptions.NotFoundException;
import com.money_track.demo.repositories.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category1,category2;

    @BeforeEach
    void setup(){
        category1 = new Category("salary", TypeValue.REVENUE);
        category2 = new Category("school", TypeValue.EXPENSE);
        categoryRepository.save(category1);
        categoryRepository.save(category2);
    }

    @DisplayName("test find all categories SUCCESS")
    @Test
    void testFindAllCategories(){
        when(categoryRepository.findAll()).thenReturn(List.of(category1,category2));
        List<CategoryDTO> allCategories = categoryService.findAllCategories();

        Assertions.assertNotNull(allCategories);
        Assertions.assertEquals("salary",allCategories.getFirst().getName());
        Assertions.assertEquals("school",allCategories.get(1).getName());
    }

    @DisplayName("test find category by id SUCCESS")
    @Test
    void testFindCategoryByIdSuccess(){
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category1));
        CategoryDTO category = categoryService.findCategoryById(1L);

        Assertions.assertNotNull(category);
        Assertions.assertEquals("salary", category.getName());
    }

    @DisplayName("test find category by id FAILED")
    @Test
    void testFindCategoryByIdFailed(){
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> categoryService.findCategoryById(1L));

        Assertions.assertEquals("Não existe categoria com este ID", exception.getMessage());
    }

    @DisplayName("test create Category SUCCESS")
    @Test
    void testCreateCategorySuccess(){
        when(categoryRepository.save(any())).thenReturn(category2);
        CategoryDTO categoryDTO = categoryService.createCategory(category2);

        Assertions.assertNotNull(categoryDTO);
        Assertions.assertEquals("school", categoryDTO.getName());
    }

    @DisplayName("test update Category SUCCESS")
    @Test
    void testUpdateCategorySuccess(){
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category1));
        when(categoryRepository.save(any())).thenReturn(category1);

        CategoryDTO updatedCategory = categoryService.updateCategory(category2,1L);
        Assertions.assertNotNull(updatedCategory);
        Assertions.assertEquals("school", updatedCategory.getName());
    }

    @DisplayName("test update Category FAILED")
    @Test
    void testUpdateCategoryFailed(){
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> categoryService.updateCategory(category2,1L));

        Assertions.assertEquals("Não existe categoria com este ID", exception.getMessage());
    }

    @DisplayName("test delete Category SUCCESS")
    @Test
    void testDeleteCategorySuccess(){
        categoryService.deleteCategoryById(1L);

        verify(categoryRepository).deleteById(1L);
    }

    @DisplayName("test delete category FAILED")
    @Test
    void testDeleteCategoryFailed(){
        doThrow(new NotFoundException("Não existe categoria com este ID")).when(categoryRepository).deleteById(anyLong());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> categoryService.deleteCategoryById(1L));

        verify(categoryRepository).deleteById(1L);
        Assertions.assertEquals("Não existe categoria com este ID",exception.getMessage());
    }
}
