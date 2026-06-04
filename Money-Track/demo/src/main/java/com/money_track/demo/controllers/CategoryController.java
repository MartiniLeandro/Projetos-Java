package com.money_track.demo.controllers;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.CategoryDTO;
import com.money_track.demo.entities.enums.TypeValue;
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
    public ResponseEntity<List<CategoryDTO>> findAllCategories(){
        return ResponseEntity.ok().body(categoryService.findGlobalAndUserCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findUserById(@PathVariable @Valid Long id){
        return ResponseEntity.ok().body(categoryService.findCategoryById(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<CategoryDTO>> findAllCategoriesByTypeValue(@RequestParam(required = false) TypeValue typeValue){
        return ResponseEntity.ok().body(categoryService.findAllCategoriesByTypeValue(typeValue));
    }

    @PostMapping("/admin/createCategory")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CategoryDTO category){
        return ResponseEntity.ok().body(categoryService.createCategory(category));
    }

    @PutMapping("admin/updateCategory/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody @Valid CategoryDTO category, @PathVariable @Valid Long id){
        return ResponseEntity.ok().body(categoryService.updateCategory(category,id));
    }

    @DeleteMapping("admin/deleteCategory/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable @Valid Long id){
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }
}
