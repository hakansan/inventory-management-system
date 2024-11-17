package com.oneamz.inventory.service;

import com.oneamz.inventory.exception.ApiResponseCode;
import com.oneamz.inventory.exception.InventoryException;
import com.oneamz.inventory.model.dto.CategoryRequest;
import com.oneamz.inventory.model.dto.CategoryResponse;
import com.oneamz.inventory.model.entity.Category;
import com.oneamz.inventory.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> getAllCategories() {
        log.info("Retrieving all categories");
        return categoryRepository.findByActiveTrue().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        log.info("Retrieving category with ID: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Category with ID {} not found", id);
                    return new InventoryException(ApiResponseCode.CATEGORY_NOT_FOUND);
                });
        return convertToResponse(category);
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        log.info("Creating new category with name: {}", categoryRequest.getName());
        Category category = new Category();
        category.setName(categoryRequest.getName());
        Category savedCategory = categoryRepository.save(category);
        log.info("Category created with ID: {}", savedCategory.getId());
        return convertToResponse(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        log.info("Updating category with ID: {}", id);
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Category with ID {} not found", id);
                    return new InventoryException(ApiResponseCode.CATEGORY_NOT_FOUND);
                });

        existingCategory.setName(categoryRequest.getName());
        Category updatedCategory = categoryRepository.save(existingCategory);
        log.info("Category with ID {} updated successfully", updatedCategory.getId());
        return convertToResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("Deactivating category with ID: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Category with ID {} not found", id);
                    return new InventoryException(ApiResponseCode.CATEGORY_NOT_FOUND);
                });

        category.setActive(false);
        categoryRepository.save(category);
        log.info("Category with ID {} deactivated successfully", id);
    }

    private CategoryResponse convertToResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        return response;
    }
}