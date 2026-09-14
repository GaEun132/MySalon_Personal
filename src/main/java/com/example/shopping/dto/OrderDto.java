package com.example.shopping.dto;

import com.example.shopping.entity.Order;
import com.example.shopping.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetailDto {
        private Long itemId;
        private int count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOrderRequest {
        private Long addressId;
        private List<OrderDetailDto> orderDetails;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderCompleteResponse {
        private Long orderId;
        private String userName;
        private Integer totalPrice;
        private LocalDateTime orderedAt;

        public static OrderCompleteResponse fromEntity(Order order) {
            return OrderCompleteResponse.builder()
                    .orderId(order.getOrderId())
                    .userName(order.getUser().getUserName())
                    .totalPrice(Order.getTotalPrice(order.getOrderDetails()))
                    .orderedAt(order.getOrderedAt())
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderHistoryListResponse {
        private List<OrderHistoryResponse> orderList;
        private int totalPages;

        public static OrderHistoryListResponse fromEntity(Page<Order> orders, Map<Long, List<OrderDetail>> detailsMap) {
            List<Order> orderList = orders.getContent();

            return OrderHistoryListResponse.builder()
                    .orderList(orderList.stream()
                            .map(order ->OrderHistoryResponse.fromEntity(order, detailsMap) )
                            .collect(Collectors.toList()))
                    .totalPages(orders.getTotalPages())
                    .build();

        }
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderHistoryResponse {
        private Long orderNum; //주문 번호
        private LocalDateTime orderedAt; //주문 일자
        private List<OrderDetailResponse> orderDetails;

        public static OrderHistoryResponse fromEntity(Order order, Map<Long, List<OrderDetail>> detailsMap) {

            List<OrderDetail> limitedDetails = detailsMap.getOrDefault(order.getOrderId(), Collections.emptyList());

            return OrderHistoryResponse.builder()
                    .orderDetails(limitedDetails.stream()
                            .map(OrderDetailResponse::fromEntity)
                            .collect(Collectors.toList()))
                    .orderNum(order.getOrderId())
                    .orderedAt(order.getOrderedAt())
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetailResponse {

        private Long orderDetailId; //주문 상세 번호
        private String productName; //상품명
        private int count; //주문 수량
        private String size; //사이즈
        private String color; //색상
        private Integer price; // 가격

        public static OrderDetailResponse fromEntity(OrderDetail orderDetail) {
            return OrderDetailResponse.builder()
                    .orderDetailId(orderDetail.getOrderDetailId())
                    .productName(orderDetail.getItem().getProduct().getName())
                    .count(orderDetail.getCount())
                    .size(orderDetail.getItem().getSize())
                    .color(orderDetail.getItem().getColor())
                    .price(orderDetail.getItem().getProduct().getPrice())
                    .build();
        }
    }


}
