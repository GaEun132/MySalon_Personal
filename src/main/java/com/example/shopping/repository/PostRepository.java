package com.example.shopping.repository;

import com.example.shopping.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p LEFT JOIN p.postImages pi ON pi.sortOrder = 1 ORDER BY p.createdAt desc, p.postId desc")
    Page<Post> findAllOrderByCreatedAtDesc(Pageable pageable);

    Long countAllByUserUserId(Long userId);
    @Query("SELECT p FROM Post p  WHERE p.user.userId = :userId order by p.createdAt desc, p.postId desc ")
    Slice<Post> findByUserUserId(Long userId, Pageable pageable);
}
