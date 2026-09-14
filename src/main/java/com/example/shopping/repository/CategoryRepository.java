package com.example.shopping.repository;

import com.example.shopping.dto.CategoryDto;
import com.example.shopping.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentIsNull();

    @Query(value = """
WITH RECURSIVE descendants AS (
    SELECT category_id, name, parent_id, 1 AS depth FROM category WHERE category_id = :categoryId
    UNION ALL
    SELECT c.category_id, c.name, c.parent_id, d.depth + 1
    FROM category c
    JOIN descendants d ON c.parent_id = d.category_id
)
SELECT * FROM descendants WHERE category_id != :categoryId;
""", nativeQuery = true)
    List<CategoryDto.CategoryProjection> findAllChildCategories(@Param("categoryId") Long categoryId);

    @Query(value = """
            WITH RECURSIVE descendants AS (
                SELECT category_id
                FROM category
                WHERE category_id = :categoryId
                UNION ALL
                SELECT c.category_id
                FROM category c
                JOIN descendants d ON c.parent_id = d.category_id
            )
            SELECT category_id 
            FROM category
            WHERE category_id IN (SELECT category_id FROM descendants)
            """, nativeQuery = true)
    List<Long> findAllChildCategoryIdsByParentId(@Param("categoryId") Long categoryId);
}
