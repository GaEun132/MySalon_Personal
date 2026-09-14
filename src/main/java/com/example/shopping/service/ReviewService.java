package com.example.shopping.service;

import com.example.shopping.dto.ReviewDto;
import com.example.shopping.entity.*;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import com.example.shopping.repository.ItemRepository;
import com.example.shopping.repository.ProductRepository;
import com.example.shopping.repository.ReviewRepository;
import com.example.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ProductRepository productRepository;



    @Transactional
    public ReviewDto.CreateReviewResponse createReview(Long userId, ReviewDto.CreateReviewRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

        Review review = request.toEntity(user, item);
        Review savedReview = reviewRepository.save(review);
        return ReviewDto.CreateReviewResponse.fromEntity(savedReview);

    }

    @Transactional
    public ReviewDto.UpdateReviewResponse editReview(Long userId, Long reviewId, ReviewDto.UpdateReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
        // 리뷰 수정 권한 확인
        if (!review.getUser().getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.USER_UNAUTHORIZED);
        }
        //
        List<ReviewDto.UpdateReviewImageRequest> requestImages =
                request.getImages() == null ? List.of() : request.getImages();

        // 현재 리뷰에 달려 있는 이미지들을 id 기준으로 맵핑 (삭제 대상 판별용)
        Map<Long, ReviewImage> currentImageMap = review.getReviewImages().stream()
                .collect(Collectors.toMap(ReviewImage::getReviewImageId, image -> image));

        for (ReviewDto.UpdateReviewImageRequest dto : requestImages) {

            if (dto.getReviewImageId() == null) {
                // 신규 이미지 추가
                review.assignImage(dto.toEntity());
            } else {
                // 기존 이미지 수정 — 반드시 "이 리뷰 소속"인지 확인 후 처리
                ReviewImage existing = currentImageMap.remove(dto.getReviewImageId());
                if (existing == null) {
                    throw new BusinessException(ErrorCode.REVIEW_IMAGE_NOT_FOUND);
                }
                review.changeReviewImage(dto.getReviewImageId(), dto.getImageUrl(), dto.getSortOrder());
            }
        }

        // 요청에 포함되지 않고 남은 기존 이미지는 삭제 대상
        currentImageMap.values().forEach(review::removeImage);

        if (request.getText() != null) {
            review.changeText(request.getText());
        }
        if (request.getScore() != null) {
            review.changeScore(request.getScore());
        }

        reviewRepository.flush();
        return ReviewDto.UpdateReviewResponse.fromEntity(review);

    }
    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    @Transactional(readOnly = true)
    public ReviewDto.GetAllReviewsByUserResponse getAllReviewsByUser(Long userId, int page) {

        if (!userRepository.existsById(userId)) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(page, 5);
        Slice<Review> reviews = reviewRepository.findAllByUserUserIdOrderByCreatedAtDesc(userId, pageable);

        return ReviewDto.GetAllReviewsByUserResponse.fromEntity(reviews);

    }
    @Transactional(readOnly = true)
    public ReviewDto.GetAllReviewsByProductResponse getAllReviewsByProduct(Long productId, int page) {

        if (!productRepository.existsById(productId)) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        Pageable pageable = PageRequest.of(page, 5);
        Slice<Review> reviews = reviewRepository.findAllByProductIdOrderByCreatedAtDesc(productId, pageable);
        return ReviewDto.GetAllReviewsByProductResponse.fromEntity(reviews);
    }

    @Transactional
    public ReviewDto.ReviewListResponse getAllReviewsByProductNum(Long productNum, int page) {

        Pageable pageable = PageRequest.of(page, 3, Sort.by("updatedAt").descending());
        Slice<Review> reviews = reviewRepository.findAllByProductId(productNum, pageable);
        return ReviewDto.ReviewListResponse.fromEntity(reviews);
    }

    @Transactional(readOnly = true)
    public ReviewDto.GetAllFilteredReviewsResponse getFilteredReviews(Long productId, String color, ReviewDto.ReviewSearchCondition request, int page) {

        if (!productRepository.existsById(productId)) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(page, 4);
        Slice<Review> reviews = reviewRepository.findFilteredReviews(productId, color, request, pageable);

        return ReviewDto.GetAllFilteredReviewsResponse.fromEntity(reviews);
    }

    public ReviewDto.GetReviewInfoResponse getReviewInfoById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));
        return ReviewDto.GetReviewInfoResponse.fromEntity(review);
    }
}
