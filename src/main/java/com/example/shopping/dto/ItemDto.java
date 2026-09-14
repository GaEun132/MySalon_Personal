package com.example.shopping.dto;

import com.example.shopping.entity.Item;
import com.example.shopping.entity.Product;
import lombok.*;

public class ItemDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetItemResponse {

        private Long itemId;
        private Long productId;
        private String name;
        private Integer price;
        private int count;
        private String color;
        private String size;
        private String image;


        public static ItemDto.GetItemResponse fromEntity(Item item){

            return ItemDto.GetItemResponse.builder()
                    .itemId(item.getItemId())
                    .productId(item.getProduct().getProductId())
                    .name(item.getProduct().getName())
                    .price(item.getProduct().getPrice())
                    .color(item.getColor())
                    .size(item.getSize())
                    .count(item.getCount())
                    .image(item.getImage())
                    .build();
        }
    }
}
