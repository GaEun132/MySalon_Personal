package com.example.shopping.dto;

import com.example.shopping.entity.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class CreateProductDTO {

/*



    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderedItemDto {
        private Long itemId;
        private Long productId;
        private Long orderId;
        private String productName;
        private String description;
        private Long price;
        private String color;
        private String size;
        private int count; // stock -> count
        private String image;

        public static OrderedItemDto fromEntity(Item entity) {
            return OrderedItemDto.builder()
                    .itemId(entity.getItemId())
                    .orderId(entity.getOrderDetail().getOrder().getOrderId())
                    .productId(entity.getProduct().getProductId())
                    .productName(entity.getProduct().getName())
                    .description(entity.getProduct().getDescription())
                    .price(entity.getProduct().getPrice())
                    .color(entity.getColor())
                    .size(entity.getSize())
                    .count(entity.getOrderDetail().getCount()) // getStock() -> getCount()
                    .image(entity.getProduct().getMainImage())
                    .build();

        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SoldItemDTO {
        private Long itemId;
        private Long productId;
        private Long orderId;
        private String productName;
        private String description;
        private Long price;
        private String color;
        private String size;
        private int count; // stock -> count
        private String image;

        public static SoldItemDTO fromEntity(Item entity) {
            return SoldItemDTO.builder()
                    .itemId(entity.getItemId())
                    .orderId(entity.getOrderDetail().getOrder().getOrderId())
                    .productId(entity.getProduct().getProductId())
                    .productName(entity.getProduct().getName())
                    .description(entity.getProduct().getDescription())
                    .price(entity.getProduct().getPrice())
                    .color(entity.getColor())
                    .size(entity.getSize())
                    .count(entity.getOrderDetail().getCount()) // getStock() -> getCount()
                    .image(entity.getProduct().getMainImage())
                    .build();

        }

    }*/
}
