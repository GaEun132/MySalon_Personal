package com.example.shopping.dto;

import com.example.shopping.entity.Cart;
import com.example.shopping.entity.Item;
import com.example.shopping.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

public class CartDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetUserCartListResponse {
        private List<GetUserCartResponse> carts;
        private boolean hasNext;

        public static GetUserCartListResponse fromEntity(Slice<Cart> carts) {
            List<GetUserCartResponse> cartResponses = carts.stream()
                    .map(GetUserCartResponse::fromEntity)
                    .toList();
            return GetUserCartListResponse.builder()
                    .carts(cartResponses)
                    .hasNext(carts.hasNext())
                    .build();

        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetUserCartResponse {
        private Long cartId;
        private Long itemId;
        private String itemName;
        private Integer quantity;
        private Integer price;
        private String color;
        private String size;
        private boolean isSelected;

        public static GetUserCartResponse fromEntity(Cart cart) {
            return GetUserCartResponse.builder()
                    .cartId(cart.getCartId())
                    .itemId(cart.getItem().getItemId())
                    .itemName(cart.getItem().getProduct().getName())
                    .quantity(cart.getQuantity())
                    .price(cart.getItem().getProduct().getPrice())
                    .color(cart.getItem().getColor())
                    .size(cart.getItem().getSize())
                    .isSelected(cart.isSelected())
                    .build();
        }

    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateCartResponse {
        private Long cartId;
        private Long itemId;
        private String itemName;
        private Integer quantity;
        private Integer price;
        private String color;
        private String size;
        private boolean isSelected;

        public static CreateCartResponse fromEntity(Cart cart) {
            return CreateCartResponse.builder()
                    .cartId(cart.getCartId())
                    .itemId(cart.getItem().getItemId())
                    .itemName(cart.getItem().getProduct().getName())
                    .quantity(cart.getQuantity())
                    .price(cart.getItem().getProduct().getPrice())
                    .color(cart.getItem().getColor())
                    .size(cart.getItem().getSize())
                    .isSelected(cart.isSelected())
                    .build();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateCartRequest {
        private Long itemId;
        private Integer quantity;

        public Cart toEntity(Item item, User user, Integer quantity) {
            return Cart.builder()
                    .item(item)
                    .user(user)
                    .isSelected(true)
                    .quantity(quantity)
                    .createdAt(LocalDateTime.now())
                    .build();
        }
    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateIsSelectedResponse {
        private Long cartId;
        private boolean isSelected;

        public static UpdateIsSelectedResponse fromEntity(Cart cart) {
            return UpdateIsSelectedResponse.builder()
                    .cartId(cart.getCartId())
                    .isSelected(cart.isSelected())
                    .build();
        }
    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteCartAfterOrderRequest {
        private List<Long> cartIds;
    }
}
