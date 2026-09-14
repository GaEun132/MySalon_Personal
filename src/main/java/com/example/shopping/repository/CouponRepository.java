package com.example.shopping.repository;

import com.example.shopping.entity.Coupon;
import com.example.shopping.service.CouponService;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Coupon c WHERE c.couponId = :couponId")
    Optional<Coupon> findByIdWithPessimisticLock(@Param("couponId") Long couponId);

    // select, update 쿼리가 하나의 쿼리에 원자적으로 실행된다. 이것을 원자적 업데이트라 한다.
    @Modifying
    @Query("update Coupon c set c.issuedQuantity = c.issuedQuantity + 1 where c.couponId = :couponId")
    Integer incrementIssuedQuantity(@Param("couponId") Long couponId);


}
