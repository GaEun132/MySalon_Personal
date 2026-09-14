package com.example.shopping.service;

import com.example.shopping.dto.CategoryDto;
import com.example.shopping.entity.Category;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import com.example.shopping.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public CategoryDto.GetRootCategoryResponse getRootCategory() {
        List<Category> categories = categoryRepository.findByParentIsNull();
        return CategoryDto.GetRootCategoryResponse.fromEntity(categories);

    }

    @Transactional(readOnly = true)
    public CategoryDto.GetAllChildCategoryResponse getAllChildCategory(Long categoryId) {
        List<CategoryDto.CategoryProjection> categories = categoryRepository.findAllChildCategories(categoryId);
        return CategoryDto.GetAllChildCategoryResponse.fromEntity(categories);

    }

    @Transactional
    public CategoryDto.CreateCategoryResponse createCategory(CategoryDto.CreateCategoryRequest request) {
        Category parent = null;
        if (request.getParentId() != null) {
            parent =  categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        }

        Category category = request.toEntity(parent);
        Category savedCategory = categoryRepository.save(category);
        return CategoryDto.CreateCategoryResponse.fromEntity(savedCategory);

    }
    @Transactional
    public CategoryDto.UpdateCategoryResponse updateCategory(Long categoryId, CategoryDto.UpdateCategoryRequest request) {
        Category category =  categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        Category parent = null;

        if (request.getParentId()!= null) {
            parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        }
        category.changeName(request.getName());
        category.changeParent(parent);

        Category updatedCategory = categoryRepository.save(category);
        return CategoryDto.UpdateCategoryResponse.fromEntity(updatedCategory);

    }
    @Transactional
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    public CategoryDto.GetCategoryResponse getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        return CategoryDto.GetCategoryResponse.fromEntity(category);
    }
}
