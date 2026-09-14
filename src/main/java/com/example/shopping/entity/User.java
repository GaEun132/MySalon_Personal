package com.example.shopping.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;

@Entity
@Table(name = "users",
        uniqueConstraints = {
        @UniqueConstraint(name = "unique_id", columnNames = "id"),
        @UniqueConstraint(name = "unique_user_name", columnNames = "user_name")
})

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    //아이디
    @Column(unique = true, nullable = false)
    private String id;

    //비밀번호
    @Column(nullable = false)
    private String password;

    //닉네임
    @Column(unique = true, nullable = false)
    private String userName;

    //결제 비밀번호
    @Column(nullable = false)
    private String paymentPassword;

    //프로필 이미지
    @Column
    private String profileImage;

    //키
    @Column
    private Short tall;
    //몸무게
    @Column
    private Short weight;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Builder.Default
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    //사용자 타입
    @Enumerated(EnumType.STRING)
    private UserType type;

    public void changePaymentPassword(String paymentPassword) {
        this.paymentPassword = paymentPassword;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void changeGender(Gender gender) {
        this.gender = gender;
    }
    public void changeTall(Short tall) {
        this.tall = tall;
    }

    public void changeWeight(Short weight) {
        this.weight = weight;
    }

}
