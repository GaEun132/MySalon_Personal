package com.example.shopping.dto;

import com.example.shopping.entity.Favorite;
import com.example.shopping.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetAllResponse {
    private Long productId;
    private String mainImage;
    private Integer price;
    private String name;
    private Double score;
    private Integer reviewCount;
    private boolean isLiked;


    // JPQL 매핑용 생성자 (중요: 패키지명을 포함한 경로로 쿼리에서 호출됨)
    public GetAllResponse(Long productId, String mainImage, Integer price, String name,
                          Long reviewCount, Double avgScore, boolean isLiked) {
        this.productId = productId;
        this.mainImage = mainImage;
        this.price = price;
        this.name = name;
        this.reviewCount = reviewCount.intValue(); // Long -> Integer 캐스팅
        this.score = avgScore != null ? avgScore : 0;
        this.isLiked = isLiked;
    }


    // Entity -> DTO
    public static GetAllResponse fromEntity(Review r, List<Favorite> favorites) {
        return GetAllResponse.builder()
                .productId(r.getItem().getProduct().getProductId())
                .name(r.getItem().getProduct().getName())
                .price(r.getItem().getProduct().getPrice())
                .mainImage(r.getItem().getProduct().getMainImage())
                .build();
    }

    public static GetAllResponse fromProjection(ProductDto.ProductListProjection projection) {

        Double avgScore = projection.getAvgScore();
        Double roundedAvgScore =  Math.round(avgScore * 10.0) / 10.0;
        boolean isLiked = projection.getIsLiked() != null && projection.getIsLiked() == 1;

        return GetAllResponse.builder()
                .productId(projection.getProductId())
                .mainImage(projection.getMainImage())
                .price(projection.getPrice())
                .name(projection.getProductName())
                .score(roundedAvgScore)
                .reviewCount(projection.getReviewCount())
                .isLiked(isLiked)
                .build();
    }
}
