package com.money_track.demo.services;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.CategoryDTO;
import com.money_track.demo.entities.DTO.TypeValuesDTO;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.TypeValue;
import com.money_track.demo.exceptions.AlreadyExistsException;
import com.money_track.demo.exceptions.IsNotYoursException;
import com.money_track.demo.exceptions.NotFoundException;
import com.money_track.demo.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> findGlobalAndUserCategories() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Category> allCategories = categoryRepository.findGlobalAndUserCategories(user.getId());
        return allCategories.stream().map(CategoryDTO::new).toList();
    }

    public CategoryDTO findCategoryById(Long id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe categoria com este ID"));
        if(category.getUser() != null && !Objects.equals(category.getUser().getId(), user.getId())){
            throw new IsNotYoursException("Esta categoria não é sua");
        }
        return new CategoryDTO(category);
    }

    public List<CategoryDTO> findAllCategoriesByTypeValue(TypeValue typeValue){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Category> categories = categoryRepository.findAllByTypeValueAndUserId(typeValue != null ? typeValue.name() : null, user.getId());
        return categories.stream().map(CategoryDTO::new).toList();
    }

    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(categoryRepository.existsByNameAndTypeValueAndUserId(categoryDTO.name(),categoryDTO.typeValue(), user.getId())) throw new AlreadyExistsException("Já existe uma category com este nome e tipo");
        Category category = new Category(categoryDTO);
        category.setUser(user);
        Category newCategory = categoryRepository.save(category);
        return new CategoryDTO(newCategory);
    }

    @Transactional
    public CategoryDTO updateCategory(CategoryDTO categoryDTO,Long id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category updatedCategory = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe categoria com este ID"));
        if(updatedCategory.getUser() == null || !updatedCategory.getUser().getId().equals(user.getId())) throw new IsNotYoursException("Esta categoria é global ou não é sua");
        if(categoryRepository.existsByNameAndTypeValueAndUserId(categoryDTO.name(),categoryDTO.typeValue(), user.getId()) && !updatedCategory.getName().equals(categoryDTO.name())) throw new AlreadyExistsException("Já existe uma category com este nome e tipo");
        updatedCategory.setName(categoryDTO.name());
        updatedCategory.setTypeValue(categoryDTO.typeValue());
        Category savedCategory = categoryRepository.save(updatedCategory);
        return new CategoryDTO(savedCategory);
    }

    @Transactional
    public void deleteCategoryById(Long id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Não existe categoria com este ID"));
        if(category.getUser() == null || !Objects.equals(category.getUser().getId(), user.getId())) throw new IsNotYoursException("Esta categoria é global ou não é sua");
        categoryRepository.delete(category);
    }
}
