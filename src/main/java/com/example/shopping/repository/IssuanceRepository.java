package com.example.shopping.repository;

import com.example.shopping.entity.Issuance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IssuanceRepository extends JpaRepository<Issuance, Long> {

    boolean existsByUserUserIdAndCouponCouponId(Long userId, Long couponId);
    Slice<Issuance> findByUserUserIdOrderByIssuedAtDesc(Long userId, Pageable pageable);

    @Query("SELECT i FROM Issuance i JOIN FETCH i.coupon c WHERE i.user.userId = :userId and i.status= 'ISSUED' ORDER BY i.issuedAt ASC limit 1")
    Optional<Issuance> findByUserUserIdAndStatusIsIssued(Long userId);
    @Query("SELECT i FROM Issuance i JOIN FETCH i.coupon c WHERE i.user.userId = :userId and i.status= 'USED' or i.status= 'EXPIRED' ORDER BY i.issuedAt DESC, i.issuanceId DESC")
    Slice<Issuance> findByUserUserIdAndStatus_USEDAndStatus_EXPIREDOrderByIssuedAtDescIssuanceIdDesc(Long userId, Pageable pageable);
    @Query("SELECT i FROM Issuance i JOIN FETCH i.coupon c WHERE i.user.userId = :userId and i.status= 'ISSUED' ORDER BY i.issuedAt DESC, i.issuanceId DESC")
    Slice<Issuance>  findByUserUserIdAndStatus_ISSUEDOrderByIssuedAtDescIssuanceIdDesc(Long userId, Pageable pageable);
}
