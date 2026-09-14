package com.example.shopping.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "review_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long reviewImageId;

    String imageUrl;

    Integer sortOrder;

    @JoinColumn(name = "review_id", foreignKey = @ForeignKey(name = "review_image_fk_review_id"))
    @ManyToOne(fetch = FetchType.LAZY)
    Review review;

    public void assignReview(Review review) {
        this.review = review;
    }

    public void changeSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    public void changeImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
