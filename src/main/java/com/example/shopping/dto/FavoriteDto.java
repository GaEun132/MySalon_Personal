package com.example.shopping.dto;


import com.example.shopping.entity.Favorite;
import com.example.shopping.entity.Product;
import com.example.shopping.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FavoriteDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ClickFavoriteRequest {

        @NotNull(message = "사용자 번호는 필수 입력 항목입니다.")
        private Long userId;

        @NotNull(message = "상품 번호는 필수 입력 항목입니다.")
        private Long productId;

        public Favorite toEntity(User user, Product product) {
            return Favorite.builder()
                    .user(user)
                    .product(product)
                    .createdAt(LocalDateTime.now())
                    .build();
        }
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class  GetUserFavoriteListResponse {
        List<GetUserFavoriteResponse> favorites;
        private boolean hasNext;

        public static GetUserFavoriteListResponse fromEntity(Slice<Favorite> favorites) {
            return GetUserFavoriteListResponse.builder()
                    .favorites(favorites.stream()
                            .map(FavoriteDto.GetUserFavoriteResponse::fromEntity)
                            .collect(Collectors.toList()))
                    .hasNext(favorites.hasNext())
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetUserFavoriteResponse {
        private Long favoriteId;
        private Long userId;
        private Long productId;
        private String productName;
        private String productImage;
        private Integer productPrice;
        public static GetUserFavoriteResponse fromEntity(Favorite favorite) {
            return GetUserFavoriteResponse.builder()
                    .favoriteId(favorite.getFavoriteId())
                    .userId(favorite.getUser().getUserId())
                    .productId(favorite.getProduct().getProductId())
                    .productName(favorite.getProduct().getName())
                    .productImage(favorite.getProduct().getMainImage())
                    .productPrice(favorite.getProduct().getPrice())
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ClickFavoriteResponse {
        private Long productId;
        private Long likeCount;
        private boolean isLike;

        public static ClickFavoriteResponse fromEntity(Product product, boolean isLike) {
            return ClickFavoriteResponse.builder()
                    .likeCount(product.getLikeCount())
                    .productId(product.getProductId())
                    .isLike(isLike)
                    .build();
        }
    }


}
