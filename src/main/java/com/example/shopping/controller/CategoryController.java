package com.example.shopping.controller;

import com.example.shopping.dto.CategoryDto;
import com.example.shopping.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto.CreateCategoryResponse> createCategory(@RequestBody CategoryDto.CreateCategoryRequest request) {
        CategoryDto.CreateCategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{categoryId}")
    public ResponseEntity<CategoryDto.UpdateCategoryResponse> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDto.UpdateCategoryRequest request) {
        CategoryDto.UpdateCategoryResponse response = categoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto.GetCategoryResponse> getCategory(@PathVariable Long categoryId) {
        CategoryDto.GetCategoryResponse response = categoryService.getCategory(categoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/root")
    public ResponseEntity<CategoryDto.GetRootCategoryResponse> getRootCategory() {
        CategoryDto.GetRootCategoryResponse response =  categoryService.getRootCategory();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{categoryId}/child")
    public ResponseEntity<CategoryDto.GetAllChildCategoryResponse> getAllChildCategory(@PathVariable Long categoryId) {
        CategoryDto.GetAllChildCategoryResponse response =  categoryService.getAllChildCategory(categoryId);
        return ResponseEntity.ok(response);
    }
}
