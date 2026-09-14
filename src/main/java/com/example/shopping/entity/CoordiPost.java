package com.example.shopping.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coordi_post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CoordiPost { // 일반 게시물과 달리 댓글을 달수없음. 하트를 누를수 있음. 사진 한장만 첨부 가능

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coordi_post_id")
    private Long coordiPostId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private Long likeCount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(name = "coordi_post_fk_user_id"))
    private User user;

    private String coordiPostImage;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public void decreaseLikeCount() {
        this.likeCount--;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }
}