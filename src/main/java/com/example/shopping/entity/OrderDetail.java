package com.example.shopping.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_detail",uniqueConstraints = {
        @UniqueConstraint(name = "unique_order_id_item_id", columnNames = {"order_id", "item_id"}
        )
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class OrderDetail{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long orderDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "order_detail_fk_item_id"))
    private Item item;

    @Column(nullable = false)
    private Integer count;

    @Column(nullable = false)
    private Integer orderPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",foreignKey = @ForeignKey(name = "order_detail_fk_order_id"))
    private Order order;

    public void assignOrder(Order order) {
        this.order = order;
    }

    public static OrderDetail createOrderDetail(Item item, int orderPrice, int count) {
        OrderDetail orderDetail = OrderDetail.builder()
                .item(item)
                .orderPrice(orderPrice)
                .count(count)
                .build();
        orderDetail.getItem().removeStock(count); // 주문 수량만큼 재고 감소

        return orderDetail;
    }
    // 취소
    public void cancel() {
        this.getItem().addStock(count);
    }
    // 총 주문 가격
    public int getTotalPrice() {
        return orderPrice * count;
    }

}
