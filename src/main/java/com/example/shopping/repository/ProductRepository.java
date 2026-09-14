package com.example.shopping.repository;


import com.example.shopping.dto.GetAllResponse;
import com.example.shopping.dto.ProductDto;
import com.example.shopping.entity.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    List<Product> findByUserUserId(Long userId);
    List<Product> findByUser(User user);

    @Query("""
        SELECT new com.example.shopping.dto.GetAllResponse(
            p.productId,
            p.mainImage,
            p.price,
            p.name,
            COUNT(r.reviewId),
            COALESCE(AVG(r.score), 0.0),
            CASE WHEN COUNT(f) > 0 THEN true ELSE false END
        )
        FROM Product p
        JOIN Item i ON p.productId = i.product.productId
        LEFT JOIN Review r ON i.itemId = r.item.itemId
        LEFT JOIN Favorite f ON p.productId = f.product.productId AND f.user.userId = :userId
        GROUP BY p.productId
        ORDER BY COALESCE(AVG(r.score), 0.0) ASC
        """)
    Slice<GetAllResponse> findTop8ByAvgScoreByJoin(Pageable pageable, @Param("userId") Long userId);

    @Query("""
        SELECT pd.product.productId
        FROM Item pd
        LEFT JOIN Review r ON pd.itemId = r.item.itemId
        GROUP BY pd.product.productId
        ORDER BY r.score ASC
        """)
    Slice<Long> findTop8ProductIds(Pageable pageable);



    @Query("""
    SELECT new com.example.shopping.dto.GetAllResponse(
        p.productId,
        p.mainImage,
        p.price,
        p.name,
        COUNT(distinct r.reviewId),
        COALESCE(AVG(r.score), 0.0),
        CASE WHEN COUNT(distinct f) > 0 THEN true ELSE false END
    )
    FROM Product p
    JOIN Item pd ON p.productId = pd.product.productId
    LEFT JOIN Review r ON pd.itemId = r.item.itemId
    LEFT JOIN Favorite f ON p.productId = f.product.productId AND f.user.userId = :userId
    WHERE p.productId IN :productIds
    GROUP BY p.productId
    """)
    List<GetAllResponse> findProductsDetailsByIds(
            @Param("productIds") List<Long> productIds,
            @Param("userId") Long userId
    );

    @Query(value = """
        SELECT
            target.product_id AS productId,
            p.main_image AS mainImage,
            p.price AS price,
            p.name AS productName,
            target.review_count AS reviewCount,
            target.avg_score AS avgScore,
            EXISTS (
                SELECT 1
                FROM favorite f
                WHERE f.product_id = target.product_id AND f.user_id = :userId
            ) AS isLiked
        FROM (
            SELECT
                i.product_id,
                COUNT(r.review_id) AS review_count,
                AVG(r.score) AS avg_score
            FROM item i
            LEFT JOIN review r ON i.item_id = r.item_id
            GROUP BY i.product_id
            ORDER BY avg_score DESC, i.product_id ASC
            LIMIT :limit OFFSET :offset
        ) target
        JOIN product p ON p.product_id = target.product_id
        """, nativeQuery = true)
    Slice<ProductDto.ProductListProjection> findProductListWithMetrics(
            @Param("userId") Long userId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );


    @EntityGraph(attributePaths = {"items"})
    Optional<Product> findById(Long productId);

    @Query(value = """
    SELECT p.product_id, p.name, p.price,
           COALESCE(rv.review_count, 0)  AS review_count,
           COALESCE(rv.avg_score, NULL)  AS avg_score,
           COALESCE(sl.order_count, 0)   AS order_count,
           COALESCE(f.wished, FALSE)     AS wished
    FROM product p
             LEFT JOIN (
        SELECT i.product_id, COUNT(*) AS review_count, AVG(r.score) AS avg_score
        FROM review r JOIN item i ON r.item_id = i.item_id
        GROUP BY i.product_id
        
    ) rv ON rv.product_id = p.product_id
             LEFT JOIN (
        SELECT i.product_id, sum(od.count) AS order_count
        FROM order_detail od JOIN item i ON od.item_id = i.item_id
        GROUP BY i.product_id
    ) sl ON sl.product_id = p.product_id
             LEFT JOIN (
        SELECT product_id, TRUE AS wished
        FROM favorite
        WHERE user_id = :userId
    ) f ON f.product_id = p.product_id
    WHERE p.category_id in :categoryIds
    ORDER BY COALESCE(sl.order_count, 0) DESC, p.product_id DESC
    limit :limit offset :offset
        """, nativeQuery = true)
    Slice<ProductDto.ProductListProjection> findProductListOrderBySalesCount(
            @Param("userId") Long userId,
            @Param("categoryIds") List<Long> categoryIds,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
    
    
    
    
}
