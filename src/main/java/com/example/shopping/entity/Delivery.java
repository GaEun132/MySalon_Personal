package com.example.shopping.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "delivery")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "delivery_fk_order_id"))
    private Order order;

    private String city;

    private String street;

    private String zipcode;


    protected void assignOrder(Order order) {
        this.order = order;
    }

    public static Delivery createDelivery (Address address) {
        return Delivery.builder()
                .status(DeliveryStatus.READY)
                .city(address.getCity())
                .street(address.getStreet())
                .zipcode(address.getZipcode())
                .build();
    }
}
