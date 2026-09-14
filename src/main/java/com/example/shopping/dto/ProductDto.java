package com.example.shopping.dto;

import com.example.shopping.entity.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateProductRequest {

        @NotBlank(message = "상품 이름은 필수입니다.")
        private String productName;
        @NotNull(message = "가격은 필수입니다.")
        private Integer price;
        @NotNull(message = "배송비는 필수입니다.")
        private Long deliveryFee;
        private String description;
        private String mainImage;
        private Long categoryId;
        private List<CreateItemRequest> items;

        public Product toEntity(User user, Category category, Item... items) {

                Product product = Product.builder()
                        .user(user)
                        .name(this.productName)
                        .price(this.price)
                        .deliveryPrice(this.deliveryFee)
                        .mainImage(this.mainImage)
                        .description(this.description)
                        .likeCount(0L)
                        .category(category)
                        .build();

                if (items!= null) {
                    for (Item item: items) {
                        product.assignItem(item);
                    }
                }
                return product;
            }
        }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateItemRequest {

        private int count;
        private String color;
        private String size;
        private String image;

        public Item toEntity() {
            return Item.builder()
                    .color(this.color)
                    .size(this.size)
                    .count(this.count)
                    .image(this.image)
                    .build();
        }

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateProductResponse {
        private Long productId;
        private Long userId;
        private String productName;
        private Integer price;
        private Long deliveryPrice;
        private String mainImage;
        private String description;
        private Long categoryId;
        private Long likeCount;
        private List<CreateItemResponse> items;

        // Entity -> DTO
        public static CreateProductResponse fromEntity(Product product) {

            return CreateProductResponse.builder()
                    .deliveryPrice(product.getDeliveryPrice())
                    .productId(product.getProductId())
                    .userId(product.getUser().getUserId())
                    .productName(product.getName())
                    .price(product.getPrice())
                    .mainImage(product.getMainImage())
                    .description(product.getDescription())
                    .categoryId(product.getCategory().getCategoryId())
                    .likeCount(product.getLikeCount())
                    .items(product.getItems() != null ? product.getItems().stream()
                            .map(CreateItemResponse::fromEntity)
                            .collect(Collectors.toList()) : Collections.emptyList())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateItemResponse {

        private Long itemId;
        private int count;
        private String color;
        private String size;
        private String image;


        public static CreateItemResponse fromEntity(Item Item){

            return CreateItemResponse.builder()
                    .itemId(Item.getItemId())
                    .color(Item.getColor())
                    .size(Item.getSize())
                    .count(Item.getCount())
                    .image(Item.getImage())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateProductRequest {

        @NotBlank(message = "상품 이름은 필수입니다.")
        private String productName;
        @NotBlank(message = "가격은 필수입니다.")
        private Integer price;
        @NotBlank(message = "배송비는 필수입니다.")
        private Long deliveryFee;
        private Long userId;
        private String description;
        private String mainImage;
        private Long categoryId;
        private List<UpdateReviewRequest> items;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateReviewRequest {

        private Long itemId;
        private int count;
        private String color;
        private String size;
        private String image;


        public static UpdateItemResponse fromEntity(Item Item){

            return UpdateItemResponse.builder()
                    .color(Item.getColor())
                    .size(Item.getSize())
                    .count(Item.getCount())
                    .image(Item.getImage())
                    .build();
        }
        public Item toEntity() {
            return Item.builder()
                    .color(this.color)
                    .size(this.size)
                    .count(this.count)
                    .image(this.image)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateProductResponse {
        private Long productId;
        private Long userId;
        private String productName;
        private Integer price;
        private Long deliveryPrice;
        private String mainImage;
        private String description;
        private Long categoryId;
        private List<UpdateItemResponse> items;

        public static UpdateProductResponse fromEntity(Product savedProduct) {
            return UpdateProductResponse.builder()
                    .deliveryPrice(savedProduct.getDeliveryPrice())
                    .productId(savedProduct.getProductId())
                    .userId(savedProduct.getUser().getUserId())
                    .productName(savedProduct.getName())
                    .price(savedProduct.getPrice())
                    .mainImage(savedProduct.getMainImage())
                    .description(savedProduct.getDescription())
                    .categoryId(savedProduct.getCategory().getCategoryId())
                    .items(savedProduct.getItems() != null ? savedProduct.getItems().stream()
                            .map(UpdateItemResponse::fromEntity)
                            .collect(Collectors.toList()) : Collections.emptyList())
                    .build();
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateItemResponse {

        private Long itemId;
        private int count;
        private String color;
        private String size;
        private String image;


        public static UpdateItemResponse fromEntity(Item item){

            return UpdateItemResponse.builder()
                    .itemId(item.getItemId())
                    .color(item.getColor())
                    .size(item.getSize())
                    .count(item.getCount())
                    .image(item.getImage())
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetProductItemResponse {

        private Long productId;
        private String name;
        private Integer price;
        private Long deliveryFee;
        private String description;
        private Long likeCount;
        private List<GetItemResponse> items;

        public static GetProductItemResponse fromEntity(Product product) {
            return GetProductItemResponse.builder()
                    .deliveryFee(product.getDeliveryPrice())
                    .productId(product.getProductId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .description(product.getDescription())
                    .likeCount(product.getLikeCount())
                    .items(product.getItems() != null ? product.getItems().stream()
                            .map(GetItemResponse::fromEntity)
                            .collect(Collectors.toList()) : Collections.emptyList())
                    .build();
        }
    }



    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetItemResponse {

        private Long itemId;
        private int count;
        private String color;
        private String size;
        private String image;


        public static GetItemResponse fromEntity(Item item){

            return GetItemResponse.builder()
                    .itemId(item.getItemId())
                    .color(item.getColor())
                    .size(item.getSize())
                    .count(item.getCount())
                    .image(item.getImage())
                    .build();
        }
    }



    public interface ProductListProjection {
        Long getProductId();
        String getMainImage();
        Integer getPrice();
        String getProductName();
        Integer getReviewCount();
        Double getAvgScore(); // SQL의 avg_score(소수점)를 받기 위함
        Integer getIsLiked();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductSearchCondition {
        String sortType;
    }
}
