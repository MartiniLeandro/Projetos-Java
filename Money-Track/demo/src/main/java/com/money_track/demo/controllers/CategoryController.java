package com.money_track.demo.controllers;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.CategoryDTO;
import com.money_track.demo.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> findAllCategories(@RequestParam Integer page, @RequestParam Integer size){
        return ResponseEntity.ok().body(categoryService.findAllCategories(page,size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findUserById(@PathVariable @Valid Long id){
        return ResponseEntity.ok().body(categoryService.findCategoryById(id));
    }

    @PostMapping("/admin/createCategory")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid Category category){
        return ResponseEntity.ok().body(categoryService.createCategory(category));
    }

    @PutMapping("admin/updateCategory/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody @Valid Category category, @PathVariable @Valid Long id){
        return ResponseEntity.ok().body(categoryService.updateCategory(category,id));
    }

    @DeleteMapping("admin/deleteCategory/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable @Valid Long id){
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }
}
