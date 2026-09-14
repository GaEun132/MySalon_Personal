package com.example.shopping.dto;

import com.example.shopping.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class CategoryDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetRootCategoryResponse {
        private List<CategoryInfoDto> categories;

        public static GetRootCategoryResponse fromEntity(List<Category> categories) {
            List<CategoryInfoDto> dtos = categories.stream()
                    .map(CategoryInfoDto::fromEntity)
                    .toList();
            return GetRootCategoryResponse.builder()
                    .categories(dtos)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetAllChildCategoryResponse {
        private List<CategoryInfoWithDepthDto> categories;

        public static GetAllChildCategoryResponse fromEntity(List<CategoryProjection> p) {

            List<CategoryInfoWithDepthDto> categoryDtos = p.stream()
                    .map(CategoryInfoWithDepthDto::fromProjection)
                    .toList();
            return GetAllChildCategoryResponse.builder()
                    .categories(categoryDtos)
                    .build();
        }
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryInfoDto {
        private Long categoryId;
        private String name;
        private Long parentId;

        public static CategoryInfoDto fromEntity(Category c) {

            return CategoryInfoDto.builder()
                    .categoryId(c.getCategoryId())
                    .name(c.getName())
                    .parentId(c.getParent() != null ? c.getParent().getCategoryId() : null)
                    .build();

        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryInfoWithDepthDto {
        private Long categoryId;
        private String name;
        private Long parentId;
        private int depth;

        public static CategoryInfoWithDepthDto fromProjection(CategoryDto.CategoryProjection projection) {

            return CategoryInfoWithDepthDto.builder()
                    .categoryId(projection.getCategoryId())
                    .name(projection.getName())
                    .parentId(projection.getParentId())
                    .depth(projection.getDepth())
                    .build();
        }
    }

    public interface CategoryProjection {
        Long getCategoryId();
        String getName();
        Long getParentId();
        int getDepth();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCategoryResponse {
        private String name;
        private Long parentId;

        public static CreateCategoryResponse fromEntity(Category savedCategory) {
            return CreateCategoryResponse.builder()
                    .name(savedCategory.getName())
                    .parentId(savedCategory.getParent() != null ? savedCategory.getParent().getCategoryId() : null)
                    .build();
        }
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCategoryRequest {

        @NotBlank
        private String name;
        private Long parentId;

        public Category toEntity(Category parent) {
            return Category.builder()
                    .name(this.name)
                    .parent(parent)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCategoryResponse {
        Long categoryId;
        private String name;
        private Long parentId;

        public static UpdateCategoryResponse fromEntity(Category updatedCategory) {
            return UpdateCategoryResponse.builder()
                    .categoryId(updatedCategory.getCategoryId())
                    .name(updatedCategory.getName())
                    .parentId(updatedCategory.getParent() != null ? updatedCategory.getParent().getCategoryId() : null)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCategoryRequest {
        private String name;
        private Long parentId;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetCategoryResponse {
        private Long categoryId;
        private String name;
        private Long parentId;

        public static GetCategoryResponse fromEntity(Category category) {
            return GetCategoryResponse.builder()
                    .categoryId(category.getCategoryId())
                    .name(category.getName())
                    .parentId(category.getParent() != null ? category.getParent().getCategoryId() : null)
                    .build();
        }
    }
}
