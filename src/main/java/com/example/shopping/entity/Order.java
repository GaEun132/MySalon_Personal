package com.example.shopping.entity;

import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;


    @CreationTimestamp
    @Column(name = "ordered_at", updatable = false)
    private LocalDateTime orderedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(name = "order_fk_user_id"))
    private User user;

    @Column(nullable = false)
    Integer totalPrice;

    @OneToOne(mappedBy = "order")
    private Delivery delivery;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    OrderStatus orderStatus = OrderStatus.ORDERED;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public void addUser(User user) {
        this.user = user;
    }


    // OrderDetail 양방향 매핑
    private void addOrderDetails(OrderDetail orderDetail) {
        this.orderDetails.add(orderDetail);
        orderDetail.assignOrder(this);
    }
    // Delivery 양방향 매핑
    public void addDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.assignOrder(this);
    }
    private void changeStatus(OrderStatus status) {
        this.orderStatus = status;
    }

    // 주문 생성
    public static Order createOrder(User user, Delivery delivery, OrderDetail... orderDetails) {

        Order order = Order.builder()
                .user(user)
                .orderStatus(OrderStatus.ORDERED)
                .orderedAt(LocalDateTime.now())
                .totalPrice(getTotalPrice(List.of(orderDetails)))
                .build();
        for (OrderDetail orderDetail : orderDetails) {
            order.addOrderDetails(orderDetail);
        }
        order.addDelivery(delivery);

        return order;

    }
    // 주문 취소
    public void cancel() {
        // 배송이 완료된 경우 주문 취소 불가
        if (delivery.getStatus() == DeliveryStatus.COMPLETED) {
            throw new BusinessException(ErrorCode.ALREADY_DELIVERED);
        }
        // 주문 상태를 취소로 변경
        this.changeStatus(OrderStatus.CANCELED);
        // 주문한 상품의 수량만큼 상품의 재고를 늘린다.
        for (OrderDetail orderDetail : this.orderDetails) {
            orderDetail.cancel();
        }
    }

    public static int getTotalPrice(List<OrderDetail> orderDetails) {
        int totalPrice = 0;
        for (OrderDetail orderDetail : orderDetails) {
            totalPrice += orderDetail.getTotalPrice();
        }
        return totalPrice;
    }



}
