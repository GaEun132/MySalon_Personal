package com.example.shopping.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "coordi_post_like",uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_id_post_id", columnNames = {"user_id", "coordi_post_id"}
        )
})
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CoordiPostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coordi_post_like_id")
    private Long coordiPostLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordi_post_id")
    private CoordiPost coordiPost;

    private LocalDateTime createdAt;
}