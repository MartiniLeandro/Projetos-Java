package com.money_track.demo.services;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.CategoryDTO;
import com.money_track.demo.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> findAllCategories(){
        List<Category> allCategories = categoryRepository.findAll();
        return allCategories.stream().map(CategoryDTO::new).toList();
    }

    public CategoryDTO findCategoryById(Long id){
        Category category = categoryRepository.findById(id).orElseThrow();
        return new CategoryDTO(category);
    }

    public CategoryDTO createCategory(Category category){
        categoryRepository.save(category);
        return new CategoryDTO(category);
    }

    public CategoryDTO updateCategory(Category category,Long id){
        Category updatedCategory = categoryRepository.findById(id).orElseThrow();
        updatedCategory.setName(category.getName());
        updatedCategory.setTypeValue(category.getTypeValue());
        categoryRepository.save(updatedCategory);
        return new CategoryDTO(updatedCategory);
    }

    public void deleteCategoryById(Long id){
        categoryRepository.deleteById(id);
    }
}
