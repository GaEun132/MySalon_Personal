package com.example.shopping.service;


import com.example.shopping.dto.FavoriteDto;
import com.example.shopping.entity.Favorite;
import com.example.shopping.entity.Product;
import com.example.shopping.entity.User;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import com.example.shopping.repository.FavoriteRepository;
import com.example.shopping.repository.ProductRepository;
import com.example.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public FavoriteDto.ClickFavoriteResponse clickFavorite(Long userId, Long productId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        if (favoriteRepository.existsByUserUserIdAndProductProductId(userId, productId)) {
            favoriteRepository.deleteByUserUserIdAndProductProductId(userId, productId);
            product.decreaseLikeCount();
            productRepository.save(product);
            return FavoriteDto.ClickFavoriteResponse.fromEntity(product, false);

        } else {
            product.increaseLikeCount();
            Favorite favorite = Favorite.builder()
                    .user(user)
                    .product(product)
                    .createdAt(LocalDateTime.now())
           .build();
            favoriteRepository.save(favorite);
            productRepository.save(product);
            return FavoriteDto.ClickFavoriteResponse.fromEntity(product, true);
        }

    }


    // 유저별 찜 목록 조회
    @Transactional(readOnly = true)
    public FavoriteDto.GetUserFavoriteListResponse getUserFavorite(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Slice<Favorite> favorites = favoriteRepository.findByUserUserId(userId, page);
        return FavoriteDto.GetUserFavoriteListResponse.fromEntity(favorites);
    }

    // 유저가 찜한 상품 개수
    @Transactional(readOnly = true)
    public Long getUserFavoriteCount(Long userNum) {
        User user = userRepository.findById(userNum)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return favoriteRepository.countByUser(user);
    }

    // 특정 상품을 찜한 유저 수
    @Transactional(readOnly = true)
    public Long getProductFavoriteCount(Long productNum) {
        return favoriteRepository.countByProductNum(productNum);
    }

}