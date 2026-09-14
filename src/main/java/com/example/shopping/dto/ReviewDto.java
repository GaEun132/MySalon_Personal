package com.example.shopping.dto;

import com.example.shopping.entity.Item;
import com.example.shopping.entity.Review;
import com.example.shopping.entity.ReviewImage;
import com.example.shopping.entity.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewDto {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReviewRequest {
        @NotNull(message = "상품 상세 정보는 필수입니다.")
        private Long itemId;
        private String text;

        @NotNull(message = "평점은 필수입니다.")
        @Min(value = 1, message = "평점은 1에서 5 사이여야 합니다.")
        @Max(value = 5, message = "평점은 1에서 5 사이여야 합니다.")
        private Short score;
        private Integer weight;
        private Integer height;

        private List<CreateReviewImageRequest> images;

        public Review toEntity(User user, Item item) {
            Review review = Review.builder()
                    .user(user)
                    .item(item)
                    .text(this.text)
                    .score(this.score)
                    .weight(this.weight)
                    .height(this.height)
                    .build();
            // request에 이미지가 없는 경우 빈 리스트 생성
            List<ReviewImage> images = this.images == null
                    ? List.of()
                    : this.images.stream()
                    .map(CreateReviewImageRequest::toEntity)
                    .toList();

            images.forEach(review::assignImage);

            return review;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReviewImageRequest {

        String imageUrl;
        Integer sortOrder;

        public static ReviewImage toEntity(CreateReviewImageRequest image) {

            return ReviewImage.builder()
                    .imageUrl(image.getImageUrl())
                    .sortOrder(image.getSortOrder())
                    .build();
        }

    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReviewResponse {

        private Long itemId;
        private Long reviewId;
        private Long userId;
        private String text;
        private Short score;
        private Integer weight;
        private Integer height;
        private List<CreateReviewImageResponse> images;

        public static CreateReviewResponse fromEntity(Review savedReview) {
            return CreateReviewResponse.builder()
                    .itemId(savedReview.getItem().getItemId())
                    .userId(savedReview.getUser().getUserId())
                    .reviewId(savedReview.getReviewId())
                    .text(savedReview.getText())
                    .score(savedReview.getScore())
                    .height(savedReview.getHeight())
                    .weight(savedReview.getWeight())
                    .images(savedReview.getReviewImages().stream()
                            .map(CreateReviewImageResponse::fromEntity)
                            .toList())
                    .build();

        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReviewImageResponse {
        String imageUrl;
        Integer sortOrder;

        public static CreateReviewImageResponse fromEntity(ReviewImage reviewImage) {
            return CreateReviewImageResponse.builder()
                    .imageUrl(reviewImage.getImageUrl())
                    .sortOrder(reviewImage.getSortOrder())
                    .build();

        }
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateReviewRequest {

        private String text;
        private Short score;
        private Integer weight;
        private Integer height;
        private List<UpdateReviewImageRequest> images;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateReviewImageRequest {
        Long reviewImageId;
        String imageUrl;
        Integer sortOrder;


        public ReviewImage toEntity() {
            return ReviewImage.builder()
                    .imageUrl(this.imageUrl)
                    .sortOrder(this.sortOrder)
                    .build();
        }
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public  static class UpdateReviewResponse {
        private Long reviewId;
        private String text;
        private Short score;
        private Integer weight;
        private Integer height;
        private List<UpdateReviewImageResponse> images;

        public static UpdateReviewResponse fromEntity(Review savedReview) {
            return UpdateReviewResponse.builder()
                    .reviewId(savedReview.getReviewId())
                    .text(savedReview.getText())
                    .score(savedReview.getScore())
                    .height(savedReview.getHeight())
                    .weight(savedReview.getWeight())
                    .images(savedReview.getReviewImages().stream()
                            .map(UpdateReviewImageResponse::fromEntity)
                            .toList())
                    .build();


        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateReviewImageResponse {
        Long reviewImageId;
        String imageUrl;
        Integer sortOrder;


        public static UpdateReviewImageResponse fromEntity(ReviewImage reviewImage) {
            return UpdateReviewImageResponse.builder()
                    .reviewImageId(reviewImage.getReviewImageId())
                    .imageUrl(reviewImage.getImageUrl())
                    .sortOrder(reviewImage.getSortOrder())
                    .build();
        }
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetAllReviewsByUserResponse {
        private List<GetReviewByUserResponse> reviews;
        private boolean hasNext;

        public static GetAllReviewsByUserResponse fromEntity(Slice<Review> reviews) {
            return GetAllReviewsByUserResponse.builder()
                    .reviews(reviews.getContent().stream().map(GetReviewByUserResponse::fromEntity).toList())
                    .hasNext(reviews.hasNext())
                    .build();
        }
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetReviewByUserResponse {
        private Long reviewId;
        private Long userId;
        private Long itemId;
        private String productName;
        private String text;
        private Short score;
        private String reviewImage;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static GetReviewByUserResponse fromEntity(Review review) {
                String reviewImage = review.getReviewImages().stream()
                .filter(i -> Integer.valueOf(1).equals(i.getSortOrder()))
                .map(ReviewImage::getImageUrl)
                .findFirst()
                .orElse(null);
            return GetReviewByUserResponse.builder()
                    .reviewId(review.getReviewId())
                    .userId(review.getUser().getUserId())
                    .itemId(review.getItem().getItemId())
                    .text(review.getText())
                    .score(review.getScore())
                    .productName(review.getItem().getProduct().getName())
                    .reviewImage(reviewImage)
                    .createdAt(review.getCreatedAt())
                    .updatedAt(review.getUpdatedAt())
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetReviewInfoResponse {
        private Long reviewId;
        private Long userId;
        private Long itemId;
        private String text;
        private Short score;
        private Integer weight;
        private Integer height;
        private List<GetReviewImageResponse> reviewImages;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static GetReviewInfoResponse fromEntity(Review review) {

            return GetReviewInfoResponse.builder()
                    .reviewId(review.getReviewId())
                    .userId(review.getUser().getUserId())
                    .itemId(review.getItem().getItemId())
                    .text(review.getText())
                    .score(review.getScore())
                    .weight(review.getWeight())
                    .height(review.getHeight())
                    .reviewImages(review.getReviewImages().stream()
                            .map(GetReviewImageResponse::fromEntity)
                            .toList())
                    .createdAt(review.getCreatedAt())
                    .updatedAt(review.getUpdatedAt())
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetReviewImageResponse {
        Long reviewImageId;
        String imageUrl;
        Integer sortOrder;

        public static GetReviewImageResponse fromEntity(ReviewImage reviewImage) {
            return GetReviewImageResponse.builder()
                    .reviewImageId(reviewImage.getReviewImageId())
                    .imageUrl(reviewImage.getImageUrl())
                    .sortOrder(reviewImage.getSortOrder())
                    .build();
        }
    }




    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetAllReviewsByProductResponse {
        private List<GetReviewByProductResponse> reviews;
        private boolean hasNext;

        public static GetAllReviewsByProductResponse fromEntity(Slice<Review> reviews) {
            return GetAllReviewsByProductResponse.builder()
                    .reviews(reviews.getContent().stream().map(GetReviewByProductResponse::fromEntity).toList())
                    .hasNext(reviews.hasNext())
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetReviewByProductResponse {
        private Long reviewId;
        private Long productId;
        private Long itemId;
        private String text;
        private Short score;
        private Integer weight;
        private Integer height;
        private List<GetReviewImageResponse> reviewImages;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static GetReviewByProductResponse fromEntity(Review review) {
            return GetReviewByProductResponse.builder()
                    .reviewId(review.getReviewId())
                    .productId(review.getItem().getProduct().getProductId())
                    .itemId(review.getItem().getItemId())
                    .text(review.getText())
                    .score(review.getScore())
                    .height(review.getHeight())
                    .weight(review.getWeight())
                    .reviewImages(review.getReviewImages().stream().map(GetReviewImageResponse::fromEntity).toList())
                    .createdAt(review.getCreatedAt())
                    .updatedAt(review.getUpdatedAt())
                    .build();
        }
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewListResponse {

        Slice<ReviewResponse> reviews;

        public static ReviewListResponse fromEntity(Slice<Review> reviews) {
            return ReviewListResponse.builder()
                    .reviews(reviews.map(ReviewResponse::fromEntity))
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewResponse {
        private Long reviewId;
        private String text;
        private Short score;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static ReviewResponse fromEntity(Review review) {
            return ReviewResponse.builder()
                    .reviewId(review.getReviewId())
                    .text(review.getText())
                    .score(review.getScore())
                    .createdAt(review.getCreatedAt())
                    .updatedAt(review.getUpdatedAt())
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewSearchCondition {

        private String size;
        private Integer minHeight;
        private Integer maxHeight;
        private Integer minWeight;
        private Integer maxWeight;
        private String sortType;

    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetAllFilteredReviewsResponse {
        private List<GetFilteredReviewsResponse> reviews;
        private boolean hasNext;

        public static GetAllFilteredReviewsResponse fromEntity(Slice<Review> reviews) {
            return GetAllFilteredReviewsResponse.builder()
                    .reviews(reviews.getContent().stream().map(GetFilteredReviewsResponse::fromEntity).toList())
                    .hasNext(reviews.hasNext())
                    .build();
        }
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetFilteredReviewsResponse {
        private Long reviewId;
        private Long itemId;
        private String text;
        private Short score;
        private Integer weight;
        private Integer height;
        private List<GetReviewImageResponse> reviewImages;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static GetFilteredReviewsResponse fromEntity(Review review) {
            return GetFilteredReviewsResponse.builder()
                    .reviewId(review.getReviewId())
                    .itemId(review.getItem().getItemId())
                    .text(review.getText())
                    .score(review.getScore())
                    .height(review.getHeight())
                    .weight(review.getWeight())
                    .reviewImages(review.getReviewImages().stream().map(GetReviewImageResponse::fromEntity).toList())
                    .createdAt(review.getCreatedAt())
                    .updatedAt(review.getUpdatedAt())
                    .build();
        }
    }
}
