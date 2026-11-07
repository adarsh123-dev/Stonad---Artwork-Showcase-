
package com.Stonad_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.Stonad_backend.entity.Category;
import com.Stonad_backend.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Save category
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Get all categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Get category by ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    // Delete category
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
    
    public Category updateCategory(Long id, Category updatedCategory) {

        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isPresent()) {

            Category existingCategory = optionalCategory.get();

            existingCategory.setName(updatedCategory.getName());

            return categoryRepository.save(existingCategory);
        }

        throw new RuntimeException("Category not found with id: " + id);
    }
}
