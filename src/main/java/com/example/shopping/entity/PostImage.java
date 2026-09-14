package com.example.shopping.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "post_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long postImageId;
    String imageUrl;

    Integer sortOrder;

    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "post_image_fk_post_id"))
    @ManyToOne(fetch = FetchType.LAZY)
    Post post;

    public void assignPost(Post post) {
        this.post = post;
    }
    public void changeSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    public void changeImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

