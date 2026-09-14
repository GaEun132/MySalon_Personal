package com.example.shopping.dto;

import com.example.shopping.entity.CoordiPost;
import com.example.shopping.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class CoordiPostDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateCoordiPostRequest {


        @NotBlank(message = "제목은 필수 입력 항목입니다.")
        private String title;

        @NotBlank(message = "본문 내용은 필수 입력 항목입니다.")
        private String text;

        private String image;

        public CoordiPost toEntity(User user) {
            return CoordiPost.builder()
                    .user(user)
                    .title(this.title)
                    .text(this.text)
                    .likeCount(0L)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .coordiPostImage(this.image)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateCoordiPostResponse {
        private Long coordiPostId;
        private Long writerId;
        private String title;
        private String text;
        private Long likeCount;
        private String image;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static CreateCoordiPostResponse fromEntity(CoordiPost post) {
            return CreateCoordiPostResponse.builder()
                    .coordiPostId(post.getCoordiPostId())
                    .writerId(post.getUser().getUserId())
                    .title(post.getTitle())
                    .text(post.getText())
                    .likeCount(post.getLikeCount())
                    .image(post.getCoordiPostImage())
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .build();
        }
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SimpleCoordiPostListResponse {
        private List<SimpleCoordiPostResponse> coordiPosts;
        private boolean hasNext;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SimpleCoordiPostResponse {
        private Long coordiPostId;
        private Long userId;
        private String image;
        private String profileImage;
        private String title;
        private String writerName;
        private Long likeCount;
        private boolean isLiked;

        public static SimpleCoordiPostResponse fromEntity(CoordiPost post, boolean isLiked) {
            return SimpleCoordiPostResponse.builder()
                    .coordiPostId(post.getCoordiPostId())
                    .userId(post.getUser().getUserId())
                    .image(post.getCoordiPostImage())
                    .profileImage(post.getUser().getProfileImage())
                    .title(post.getTitle())
                    .writerName(post.getUser().getUserName())
                    .likeCount(post.getLikeCount())
                    .isLiked(isLiked)
                    .build();
        }
        public static SimpleCoordiPostResponse fromProjection(SimpleCoordiPostProjection projection) {
            return SimpleCoordiPostResponse.builder()
                    .coordiPostId(projection.getCoordiPostId())
                    .userId(projection.getUserId())
                    .image(projection.getImage())
                    .profileImage(projection.getProfileImage())
                    .title(projection.getTitle())
                    .writerName(projection.getWriterName())
                    .likeCount(projection.getLikeCount())
                    .isLiked(projection.getIsLiked() == 1)
                    .build();
        }
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SimpleTop10CoordiPostListResponse {
        private List<SimpleCoordiPostResponse> coordiPosts;

        public static SimpleTop10CoordiPostListResponse fromProjection(List<SimpleCoordiPostProjection> posts) {
            return SimpleTop10CoordiPostListResponse.builder()
                    .coordiPosts(posts.stream()
                            .map(SimpleCoordiPostResponse::fromProjection)
                            .toList())
                    .build();
        }
    }


    public interface SimpleCoordiPostProjection {
        Long getCoordiPostId();
        Long getUserId();
        String getImage();
        String getProfileImage();
        String getTitle();
        String getWriterName();
        Long getLikeCount();
        Integer getIsLiked();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ClickCoordiPostLikeResponse {
        private Long coordiPostId;
        private Long userId;
        private Long likeCount;
        private boolean isLiked;

        public static ClickCoordiPostLikeResponse fromEntity(CoordiPost post, boolean isLiked) {
            return ClickCoordiPostLikeResponse.builder()
                    .coordiPostId(post.getCoordiPostId())
                    .userId(post.getUser().getUserId())
                    .likeCount(post.getLikeCount())
                    .isLiked(isLiked)
                    .build();
        }
    }
}
