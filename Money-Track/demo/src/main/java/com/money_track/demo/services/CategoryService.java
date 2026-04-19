package com.money_track.demo.services;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.CategoryDTO;
import com.money_track.demo.exceptions.AlreadyExistsException;
import com.money_track.demo.exceptions.NotFoundException;
import com.money_track.demo.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO){
        if(categoryRepository.existsByNameAndTypeValue(categoryDTO.name(),categoryDTO.typeValue())) throw new AlreadyExistsException("Já existe uma category com este nome e tipo");
        Category category = new Category(categoryDTO);
        Category newCategory = categoryRepository.save(category);
        return new CategoryDTO(newCategory);
    }

    @Transactional
    public CategoryDTO updateCategory(CategoryDTO categoryDTO,Long id){
        Category updatedCategory = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe categoria com este ID"));
        if(categoryRepository.existsByNameAndTypeValue(categoryDTO.name(),categoryDTO.typeValue()) && !updatedCategory.getName().equals(categoryDTO.name())) throw new AlreadyExistsException("Já existe uma category com este nome e tipo");
        updatedCategory.setName(categoryDTO.name());
        updatedCategory.setTypeValue(categoryDTO.typeValue());
        Category savedCategory = categoryRepository.save(updatedCategory);
        return new CategoryDTO(savedCategory);
    }

    @Transactional
    public void deleteCategoryById(Long id){
        if(!categoryRepository.existsById(id)) throw new NotFoundException("Não existe categoria com este ID");
        categoryRepository.deleteById(id);
    }
}
