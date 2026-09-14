package com.example.shopping.dto;


import com.example.shopping.entity.Comment;
import com.example.shopping.entity.Post;
import com.example.shopping.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

public class CommentDto {


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateCommentRequest {

        @NotNull(message = "게시글 번호는 필수 입력 항목입니다.")
        private Long postId;

        @NotBlank(message = "댓글 내용은 필수 입력 항목입니다.")
        private String text;

        public Comment toEntity(User user, Post post) {
            return Comment.builder()
                    .user(user)
                    .post(post)
                    .text(this.text)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateCommentResponse {
        private Long commentId;
        private Long userId;
        private String writerName;
        private Long postId;
        private String text;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static CreateCommentResponse fromEntity(Comment comment) {
            return CreateCommentResponse.builder()
                    .commentId(comment.getCommentId())
                    .userId(comment.getUser().getUserId())
                    .writerName(comment.getUser().getUserName())
                    .postId(comment.getPost().getPostId())
                    .text(comment.getText())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CommentResponse {
        private Long commentId;
        private Long postId;
        private Long userId;
        private String writerName;
        private String profile;
        private String text;
        private LocalDateTime updatedAt;

        public static CommentResponse fromEntity(Comment comment) {
            return CommentResponse.builder()
                    .commentId(comment.getCommentId())
                    .postId(comment.getPost().getPostId())
                    .userId(comment.getUser().getUserId())
                    .writerName(comment.getUser().getUserName())
                    .text(comment.getText())
                    .updatedAt(comment.getUpdatedAt())
                    .profile(comment.getUser().getProfileImage())
                    .build();
        }
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetUserCommentResponse {
        private Long commentId;
        private Long postId;
        private String text;
        private LocalDateTime writingDate;

        public static GetUserCommentResponse fromEntity(Comment comment) {
            return GetUserCommentResponse.builder()
                    .commentId(comment.getCommentId())
                    .postId(comment.getPost().getPostId())
                    .text(comment.getText())
                    .writingDate(comment.getUpdatedAt())
                    .build();
        }
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetUserCommentListResponse {
        private List<GetUserCommentResponse> comments;
        private boolean hasNext;

        public static GetUserCommentListResponse fromEntity(Slice<Comment> comments) {

            return GetUserCommentListResponse.builder()
                    .comments(comments.getContent().stream()
                            .map(GetUserCommentResponse::fromEntity)
                            .toList())
                    .hasNext(comments.hasContent())
                    .build();
        }
    }
}
