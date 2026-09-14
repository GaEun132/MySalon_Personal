package com.example.shopping.repository;


import com.example.shopping.entity.CoordiPostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordiPostLikeRepository extends JpaRepository<CoordiPostLike, Long> {

    boolean existsByUserUserIdAndCoordiPostCoordiPostId(Long userId, Long coordiPostId);

    void deleteByUserUserIdAndCoordiPostCoordiPostId(Long userId, Long coordiPostId);
}
