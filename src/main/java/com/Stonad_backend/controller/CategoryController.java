
package com.Stonad_backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;

import com.Stonad_backend.entity.Category;
import com.Stonad_backend.repository.CategoryRepository;
import com.Stonad_backend.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Create Category
    @PostMapping
    public Category saveCategory(@RequestBody Category category) {
        return categoryService.saveCategory(category);
    }

    // Get All Categories
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // Get Category By ID
    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    // Delete Category
    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "Category deleted successfully";
    }
    

@PutMapping("/{id}")
public Category updateCategory(
        @PathVariable Long id,
        @RequestBody Category category) {

    return categoryService.updateCategory(id, category);
}

}
