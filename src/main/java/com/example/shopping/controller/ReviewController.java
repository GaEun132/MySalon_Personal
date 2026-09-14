package com.example.shopping.controller;

import com.example.shopping.dto.ReviewDto;
import com.example.shopping.security.CurrentUser;
import com.example.shopping.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto.CreateReviewResponse> createReview(@CurrentUser Long userId, @RequestBody ReviewDto.CreateReviewRequest request) {
        ReviewDto.CreateReviewResponse response = reviewService.createReview(userId, request);
        return ResponseEntity.ok(response);
    }

    // 지산이 작성한 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDto.UpdateReviewResponse> editReview(
            @CurrentUser Long userId,
            @PathVariable Long reviewId,
            @RequestBody ReviewDto.UpdateReviewRequest request) {
        return ResponseEntity.ok(reviewService.editReview(userId, reviewId, request));
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    //유저의 모든 리뷰 조회
    @GetMapping("/user")
    public ResponseEntity<ReviewDto.GetAllReviewsByUserResponse> getAllReviewsByUser(@CurrentUser Long userId, @RequestParam int page) {
        ReviewDto.GetAllReviewsByUserResponse response = reviewService.getAllReviewsByUser(userId,page);
        return ResponseEntity.ok(response);
    }

    //특정 리뷰 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto.GetReviewInfoResponse> getReviewById(@PathVariable Long reviewId) {
        ReviewDto.GetReviewInfoResponse response = reviewService.getReviewInfoById(reviewId);
        return ResponseEntity.ok(response);
    }

    // 상품 기준 리뷰 전체 조회
    @GetMapping("/product/{productId}")
    public ResponseEntity<ReviewDto.GetAllReviewsByProductResponse> getAllReviewsByProduct(
            @PathVariable Long productId, @RequestParam int page) {
        return ResponseEntity.ok(
                reviewService.getAllReviewsByProduct(productId, page)
        );
    }
    // 사이즈, 키, 몸무게로 필터링
    // 최신순, 별점순 정렬
    
    
    @GetMapping("/product/{productNum}")
    public ResponseEntity<ReviewDto.ReviewListResponse> getAllReviewsByProductNum(
            @PathVariable Long productNum, @RequestParam int page) {
        return ResponseEntity.ok(
                reviewService.getAllReviewsByProductNum(productNum, page)
        );
    }

    @PostMapping("/product/{productId}/filter")
    public ResponseEntity<ReviewDto.GetAllFilteredReviewsResponse> getFilteredReviews(@PathVariable Long productId, @RequestParam int page, @RequestParam String color, @RequestBody ReviewDto.ReviewSearchCondition request) {
        ReviewDto.GetAllFilteredReviewsResponse response = reviewService.getFilteredReviews(productId, color, request, page);
        return ResponseEntity.ok(response);
    }
    
    
}
