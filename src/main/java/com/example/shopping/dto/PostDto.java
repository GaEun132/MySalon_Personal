package com.example.shopping.dto;


import com.example.shopping.entity.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

public class PostDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreatePostRequest {


        @NotBlank(message = "제목은 필수 입력 항목입니다.")
        private String title;

        @NotBlank(message = "본문 내용은 필수 입력 항목입니다.")
        private String text;

        private List<ImageRequest> images;

        public Post toEntity(User user) {
            Post post = Post.builder()
                    .user(user)
                    .title(this.title)
                    .text(this.text)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            List<PostImage> images = this.images == null
                    ? List.of()
                    : this.images.stream()
                    .map(PostDto.ImageRequest::toEntity)
                    .toList();

            images.forEach(post::assignImage);
            return post;
        }
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImageRequest {
        String imageUrl;
        Integer sortOrder;

        public PostImage toEntity() {
            return PostImage.builder()
                    .imageUrl(this.imageUrl)
                    .sortOrder(this.sortOrder)
                    .build();
        }
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ImageResponse {
        Long postImageId;
        String imageUrl;
        Integer sortOrder;

        public static ImageResponse fromEntity(PostImage postImage) {
            return ImageResponse.builder()
                    .postImageId(postImage.getPostImageId())
                    .imageUrl(postImage.getImageUrl())
                    .sortOrder(postImage.getSortOrder())
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreatePostResponse {
        private Long postId;
        private Long writerId;
        private String title;
        private String text;
        private List<ImageResponse> images;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static CreatePostResponse fromEntity(Post post) {
            return CreatePostResponse.builder()
                    .postId(post.getPostId())
                    .writerId(post.getUser().getUserId())
                    .title(post.getTitle())
                    .text(post.getText())
                    .images(post.getPostImages().stream()
                            .map(ImageResponse::fromEntity)
                            .toList())
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .build();
        }
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetSimplePostListResponse {
        private List<GetSimplePostResponse> posts;
        private Integer totalPages;


        public static GetSimplePostListResponse fromEntity(Page<Post> posts) {
            return GetSimplePostListResponse.builder()
                    .posts(posts.stream()
                            .map(GetSimplePostResponse::fromEntity)
                            .toList())
                    .totalPages(posts.getTotalPages())
                    .build();
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetSimplePostListSliceResponse {
        private List<GetSimplePostResponse> posts;
        private boolean hasNext;


        public static GetSimplePostListSliceResponse fromEntity(Slice<Post> posts) {
            return GetSimplePostListSliceResponse.builder()
                    .posts(posts.stream()
                            .map(GetSimplePostResponse::fromEntity)
                            .toList())
                    .hasNext(posts.hasNext())
                    .build();
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetSimplePostResponse {
        private Long postId;
        private Long writerId;
        private String writerName;
        private String title;
        private LocalDateTime writingDate;
        private String imageUrl;


        public static GetSimplePostResponse fromEntity(Post post) {
            return GetSimplePostResponse.builder()
                    .postId(post.getPostId())
                    .writerId(post.getUser().getUserId())
                    .imageUrl(post.getPostImages().isEmpty() ? null : post.getPostImages().get(0).getImageUrl())
                    .title(post.getTitle())
                    .writerName(post.getUser().getUserName())
                    .writingDate(post.getUpdatedAt())
                    .build();
        }
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PostDetailResponse {
        private Long postId;
        private String title;
        private String writerName;
        private String text;
        private LocalDateTime createdAt;
        private List<ImageResponse> images;
        List<CommentDto.CommentResponse> comments;

        public static PostDetailResponse fromEntity(Post post) {
            return PostDetailResponse.builder()
                    .images(post.getPostImages().stream()
                            .map(ImageResponse::fromEntity)
                            .toList())
                    .title(post.getTitle())
                    .writerName(post.getUser().getUserName())
                    .text(post.getText())
                    .comments(post.getComments().stream().map(CommentDto.CommentResponse::fromEntity).toList())
                    .build();
        }
    }





}
