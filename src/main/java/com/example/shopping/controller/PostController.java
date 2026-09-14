package com.example.shopping.controller;


import com.example.shopping.dto.CommentDto;
import com.example.shopping.dto.PostDto;
import com.example.shopping.security.CurrentUser;
import com.example.shopping.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto.CreatePostResponse> createPost(@CurrentUser Long userId, @Valid @RequestBody PostDto.CreatePostRequest request) {
        PostDto.CreatePostResponse createPostResponse = postService.createPost(userId,request);
        return ResponseEntity.ok(createPostResponse);
    }


    @GetMapping
    public ResponseEntity<PostDto.GetSimplePostListResponse> getAllPost( @RequestParam int page) {
        PostDto.GetSimplePostListResponse response = postService.getAllSimplePost(page);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/user")
    public ResponseEntity<PostDto.GetSimplePostListSliceResponse> getUserPost(@CurrentUser Long userId, @RequestParam int page) {
        PostDto.GetSimplePostListSliceResponse response = postService.getUserPost(userId, page);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto.PostDetailResponse> getPostDetail(@PathVariable Long postId) {
        PostDto.PostDetailResponse response = postService.getPostDetail(postId);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }




}
