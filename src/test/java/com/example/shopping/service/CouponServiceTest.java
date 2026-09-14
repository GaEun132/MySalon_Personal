package com.example.shopping.service;

import com.example.shopping.entity.Coupon;
import com.example.shopping.entity.Item;
import com.example.shopping.entity.User;
import com.example.shopping.repository.CouponRepository;
import com.example.shopping.repository.IssuanceRepository;
import com.example.shopping.repository.ItemRepository;
import com.example.shopping.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
public class CouponServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    private CouponService couponService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private IssuanceRepository issuanceRepository;

    private User testUser;
    private Coupon testCoupon;
    private final List<Long> userIdList = new ArrayList<>();
    private Long savedCouponId;

    @BeforeEach
    void setUp() {
        for (int i = 0; i<100; i++) {
            testUser = userRepository.save(User.builder()
                    .userName("테스터" + i)
                    .id("test_id" + i)
                    .password("test_password")
                    .paymentPassword("test_payment_password")
                    .build());
            userIdList.add(testUser.getUserId());
        }


        testCoupon = couponRepository.save(Coupon.builder()
                .name("테스트")
                .totalQuantity(50)
                .issuedQuantity(0)
                .validityDays(7)
                .startsAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build());
        savedCouponId = testCoupon.getCouponId();
    }

    @AfterEach
    void tearDown() {

        try {
            issuanceRepository.deleteAll();
            if (!userIdList.isEmpty()) {
                for (int i=0; i<100; i++) {
                    userRepository.findById(userIdList.get(i)).ifPresent(userRepository::delete);
                }

            }
            if (savedCouponId != null) {
                couponRepository.findById(savedCouponId).ifPresent(couponRepository::delete);
            }

        } catch (Exception e) {
            log.error("테스트 데이터 정리 중 오류 발생: {}", e.getMessage());
        }
    }

    @Test
    @DisplayName("동시에 100개의 쿠폰 발급 요청이 들어올 때 쿠폰 수량이 정확하게 50개 발급된다.")
    void issue_coupon_concurrency_100_requests() throws InterruptedException {

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(100);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();

        for (int i=0; i< threadCount; i++) {
            final int index = i;
            executorService.submit(() -> {
                try {
                    couponService.issueCoupon(userIdList.get(index), savedCouponId);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    log.error("쿠폰 발급 실패: {}",e.getMessage());
                    failureCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
                    });
        }
        latch.await();
        em.clear();
        Coupon updatedCoupon = couponRepository.findById(testCoupon.getCouponId())
                .orElseThrow();
        System.out.println("성공한 쿠폰 발급 횟수: " + successCount.get());
        System.out.println("발급 횟수: " + updatedCoupon.getIssuedQuantity());
        System.out.println("실패한 쿠폰 발급 횟수: " + failureCount.get());

        // 검증: 성공한 발급 횟수: 50
        assertThat(updatedCoupon.getIssuedQuantity()).isEqualTo(50);
        assertThat(failureCount.get()).isEqualTo(50);
    }
}
