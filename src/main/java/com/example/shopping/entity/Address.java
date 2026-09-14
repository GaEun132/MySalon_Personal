package com.example.shopping.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Getter // @Data 대신 필요한 것만 선언
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    private String name;

    private String city;

    private String street;

    private String zipcode;

    private boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "address_fk_user_id"))
    User user;



    public Address createAddress(User user, String name, String city, String street, String zipcode, boolean isDefault) {
        return Address.builder()
                .user(user)
                .name(name)
                .city(city)
                .street(street)
                .zipcode(zipcode)
                .isDefault(isDefault)
                .build();
    }
    public void assignUser(User user) {
        this.user = user;
    }

    public void changeIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
