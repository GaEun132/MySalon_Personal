package com.example.shopping.repository;


import com.example.shopping.entity.Comment;
import com.example.shopping.entity.Post;
import com.example.shopping.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUser(User user);

    List<Comment> findByPost(Post post);
    @Query("SELECT c FROM Comment c  WHERE c.user.userId = :userId order by c.createdAt desc, c.commentId desc ")
    Slice<Comment> findByUserUserId(Long userId, Pageable pageable);

    Long countAllByUserUserId(Long userId);
}
