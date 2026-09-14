package com.example.shopping.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "review_fk_user_id"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id",foreignKey = @ForeignKey(name = "review_fk_item_id"))
    private Item item;

    private String text;

    @Column(nullable = false)
    private Short score;

    private Integer height;

    private Integer weight;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void assignImage(ReviewImage reviewImage) {

            this.getReviewImages().add(reviewImage);
            reviewImage.assignReview(this);

    }
    public void changeReviewImage(Long reviewImageId, String imageUrl, Integer sortOrder) {
        for (ReviewImage reviewImage: this.reviewImages) {
            if (Objects.equals(reviewImageId, reviewImage.getReviewImageId())) {
                reviewImage.changeImageUrl(imageUrl);
                reviewImage.changeSortOrder(sortOrder);
            }
        }
    }
    public void removeImage(ReviewImage reviewImage) {
        this.reviewImages.remove(reviewImage);
        reviewImage.assignReview(null);
    }

    public void changeText(String text) {
        this.text=text;
    }

    public void changeScore(Short score) {
        this.score=score;
    }

    public void changeWeight(Integer weight) {
        this.weight = weight;
    }

    public void changeHeight(Integer height) {
        this.height = height;
    }
}
