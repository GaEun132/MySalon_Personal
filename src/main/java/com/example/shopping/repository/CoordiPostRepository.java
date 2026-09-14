package com.example.shopping.repository;

import com.example.shopping.dto.CoordiPostDto;
import com.example.shopping.entity.CoordiPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoordiPostRepository extends JpaRepository<CoordiPost, Long>, CoordiPostRepositoryCustom {

    @Query("""
    SELECT 
        cp.coordiPostId AS coordiPostId,
        u.userId AS userId,
        cp.coordiPostImage AS image,
        u.profileImage AS profileImage,
        cp.title AS title,
        u.userName AS writerName,
        cp.likeCount AS likeCount,
        CASE WHEN cpl IS NOT NULL THEN 1 ELSE 0 END AS isLiked
    FROM CoordiPost cp
    JOIN cp.user u
    LEFT JOIN CoordiPostLike cpl ON cpl.coordiPost = cp AND cpl.user.userId = :userId
    ORDER BY cp.likeCount DESC
    limit 10
""")
    List<CoordiPostDto.SimpleCoordiPostProjection> findTop10ByLikeCountDesc(@Param("userId") Long userId);

}
