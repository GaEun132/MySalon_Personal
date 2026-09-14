package com.example.shopping.controller;


import com.example.shopping.dto.CommentDto;
import com.example.shopping.security.CurrentUser;
import com.example.shopping.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto.CreateCommentResponse> createComment(@CurrentUser Long userId, @RequestBody @Valid CommentDto.CreateCommentRequest request) {
        CommentDto.CreateCommentResponse response = commentService.createComment(userId, request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/user")
    public ResponseEntity<CommentDto.GetUserCommentListResponse> getUserComment(@CurrentUser Long userId, @RequestParam int page) {
        CommentDto.GetUserCommentListResponse response = commentService.getUserComment(userId, page);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
