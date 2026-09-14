package com.example.shopping.repository;

import com.example.shopping.entity.Favorite;
import com.example.shopping.entity.Product;
import com.example.shopping.entity.User;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("select f from Favorite f where f.user.userId =:userId and f.product.productId in :productIds")
    List<Favorite> findAllByProductProductNum(@Param("productIds")List<Long> productIds, @Param("userId") Long userId);


    boolean existsByUserUserIdAndProductProductId(@Param("userId") Long userId, @Param("productId") Long productId);
    void deleteByUserUserIdAndProductProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    List<Favorite> findByUser(User user);

    List<Favorite> findByProduct(Product product);

    // 유저가 찜한 상품 개수
    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.user = :user")
    Long countByUser(@Param("user") User user);

    // 특정 상품을 찜한 유저 수
    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.product.productId = :productId")
    Long countByProductNum(@Param("productId") Long productId);

    @Query("select f from Favorite f join fetch f.product where f.user.userId = :userId order by f.createdAt desc")
    Slice<Favorite> findByUserUserId(@Param("userId") Long userId, @Param("page") int page);

    Long countAllByUserUserId(Long userId);
}
