package com.money_track.demo.services;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.CategoryDTO;
import com.money_track.demo.exceptions.AlreadyExistsException;
import com.money_track.demo.exceptions.NotFoundException;
import com.money_track.demo.repositories.CategoryRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Page<CategoryDTO> findAllCategories(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Category> allCategories = categoryRepository.findAll(pageable);
        return allCategories.map(CategoryDTO::new);
    }

    public CategoryDTO findCategoryById(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe categoria com este ID"));
        return new CategoryDTO(category);
    }

    public CategoryDTO createCategory(Category category){
        if(categoryRepository.existsByNameAndTypeValue(category.getName(),category.getTypeValue())) throw new AlreadyExistsException("Já existe uma category com este nome e tipo");
        categoryRepository.save(category);
        return new CategoryDTO(category);
    }

    public CategoryDTO updateCategory(Category category,Long id){
        Category updatedCategory = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe categoria com este ID"));
        if(categoryRepository.existsByNameAndTypeValue(category.getName(),category.getTypeValue()) && !updatedCategory.getName().equals(category.getName())) throw new AlreadyExistsException("Já existe uma category com este nome e tipo");
        updatedCategory.setName(category.getName());
        updatedCategory.setTypeValue(category.getTypeValue());
        categoryRepository.save(updatedCategory);
        return new CategoryDTO(updatedCategory);
    }

    public void deleteCategoryById(Long id){
        try{
            categoryRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Não existe categoria com este ID");
        }
    }
}
