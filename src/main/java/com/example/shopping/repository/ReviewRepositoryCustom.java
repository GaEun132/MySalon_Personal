package com.example.shopping.repository;

import com.example.shopping.dto.ReviewDto;
import com.example.shopping.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReviewRepositoryCustom {

    Slice<Review> findFilteredReviews(Long productId, String color, ReviewDto.ReviewSearchCondition condition, Pageable pageable);

}
