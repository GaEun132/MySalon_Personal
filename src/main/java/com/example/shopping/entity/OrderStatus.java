package com.example.shopping.entity;

public enum OrderStatus {
    ORDERED,          // 주문 완료
    RETURN_REQUESTED, // 환불/반품 요청됨
    RETURNED,         // 환불/반품 완료
    CANCELED         // 주문 취소
}
