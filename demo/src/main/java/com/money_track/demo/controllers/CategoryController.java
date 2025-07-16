package com.money_track.demo.controllers;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.CategoryDTO;
import com.money_track.demo.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAllCategories(){
        return ResponseEntity.ok().body(categoryService.findAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findUserById(@PathVariable Long id){
        return ResponseEntity.ok().body(categoryService.findCategoryById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody Category category){
        return ResponseEntity.ok().body(categoryService.createCategory(category));
    }

    @PostMapping("update/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody Category category, @PathVariable Long id){
        return ResponseEntity.ok().body(categoryService.updateCategory(category,id));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok().build();
    }
}
