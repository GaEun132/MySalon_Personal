package com.example.shopping.service;

import com.example.shopping.dto.OrderDto;
import com.example.shopping.entity.*;
import com.example.shopping.repository.*;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AddressRepository addressRepository;

    private User testUser;
    private Item testItem;
    private Product testProduct;
    private Category testCategory;
    private Address testAddress;

    // 생성된 데이터의 ID들을 담아둘 변수
    private Long savedUserId;
    private Long savedProductId;
    private Long savedItemId;
    private Long savedCategoryId;
    private Long savedAddressId;

    @BeforeEach
    void setUp() {
        // 테스트용 유저 생성 및 저장
        testUser = userRepository.save(User.builder()
                .userName("테스터")
                        .id("test_id")
                        .password("test_password")
                        .paymentPassword("test_payment_password")
                .build());

        savedUserId = testUser.getUserId();

        // 테스트용 주소 생성 및 저장
        testAddress = addressRepository.save(Address.builder()
                .user(testUser)
                .name("테스트 주소")
                .city("테스트 시")
                .street("테스트 거리")
                .zipcode("12345")
                .isDefault(true)
                .build());
        savedAddressId = testAddress.getId();

        // 테스트용 카테고리 생성
        testCategory = categoryRepository.save(Category.builder()
                .name("테스트_카테고리")
                .parent(null)
                .build());
        savedCategoryId = testCategory.getCategoryId();

        // 테스트용 상품 생성
        testProduct = productRepository.save(Product.builder()
                .deliveryPrice(2000L)
                .description("테스트용 상품입니다.")
                .price(10000)
                .mainImage("test_main_image.jpg")
                .name("테스트 상품")
                .category(testCategory)
                .price(10000)
                .user(testUser)
                .build());

        savedProductId = testProduct.getProductId();

        // 테스트용 상품 상세 생성 (초기 재고: 100개)
        testItem = itemRepository.save(Item.builder()
                .product(testProduct)
                .image("test_image.jpg")
                .color("Red")
                .size("M")
                .count(100)
                .build());

        savedItemId = testItem.getItemId();
    }

    @AfterEach
    void tearDown() {

        try {

            if (savedItemId != null) {
                itemRepository.findById(savedItemId).ifPresent(itemRepository::delete);
            }
            if (savedProductId != null) {
                productRepository.findById(savedProductId).ifPresent(productRepository::delete);
            }
            if (savedUserId != null) {
                userRepository.findById(savedUserId).ifPresent(userRepository::delete);
            }
            if (savedCategoryId != null) {
                categoryRepository.findById(savedCategoryId).ifPresent(categoryRepository::delete);
            }
            if (savedAddressId != null) {
                addressRepository.findById(savedAddressId).ifPresent(addressRepository::delete);
            }
        } catch (Exception e) {
            log.error("테스트 데이터 정리 중 오류 발생: {}", e.getMessage());
        }
    }

    @Test
    @DisplayName("단일 스레드에서는 트랜잭션으로 정상 처리된다")
    @Transactional
    void transactionWorksInSingleThread() {
        // given
        // 주문 생성 횟수 (150번 시도)
        int numberOfCalls = 150;
        // 주문 성공 횟수 카운트
        int successCount = 0;
        // 주문 실패 횟수 카운트
        int failureCount = 0;
        // 1회 요청당 주문 수량
        int orderCountPerRequest = 1;

        // 테스트 요청 DTO 생성
        OrderDto.OrderDetailDto orderDetailDto = new OrderDto.OrderDetailDto(
                testItem.getItemId(),
                orderCountPerRequest
        );
        OrderDto.CreateOrderRequest request = new OrderDto.CreateOrderRequest(savedAddressId, List.of(orderDetailDto));

        // when
        // 단일 스레드에서 순차적으로 주문 생성을 처리
        for (int i = 0; i < numberOfCalls; i++) {
            try {
                // 비즈니스 로직 호출 (주문 생성)
                orderService.createOrder(request, testUser.getUserId());

                // 주문에 성공하면 카운트 증가
                successCount++;
            } catch (Exception e) {
                // 주문 실패 시 (예: 재고 부족) 로그 기록
                log.error("w주문 실패: {}", e.getMessage());
                failureCount++;
            }
        }

        // then
        em.clear();  // 1차 캐시를 초기화하여 DB 데이터를 새로 SELECT 하도록 유도
        // 1. DB에서 최신 상태의 ProductDetail 조회
        Item updatedItem = itemRepository.findById(testItem.getItemId())
                .orElseThrow();

        System.out.println("성공한 주문 횟수: " + successCount);
        System.out.println("남은 재고: " + updatedItem.getCount());
        System.out.println("실패한 주문 횟수: " + failureCount);

        // 2. 검증: 초기 재고 100개 - (성공한 주문 100회 * 1개) = 남은 재고 0개여야 함
        assertThat(updatedItem.getCount()).isEqualTo(0);

        // 3. 검증: 실패한 횟수는 50회여야 함 (총 150회 시도 - 성공 100회 = 실패 50회)
        assertThat(failureCount).isEqualTo(50);
    }


    @Test
    @DisplayName("동시에 100개의 주문 요청이 들어올 때 재고가 정확하게 100개 감소한다.")
    void createOrder_concurrency_100_requests() throws InterruptedException {
        // given
        int threadCount = 100; // 동시에 요청할 쓰레드 수
        int orderCountPerRequest = 1; // 1회 요청당 주문 수량

        // 동시 요청을 보낼 쓰레드 풀 생성
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        // 100개 쓰레드가 작업 완료될 때까지 대기하도록 돕는 Latch
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 테스트 요청 DTO 생성
        OrderDto.OrderDetailDto orderDetailDto = new OrderDto.OrderDetailDto(
                testItem.getItemId(),
                orderCountPerRequest
        );
        OrderDto.CreateOrderRequest request = new OrderDto.CreateOrderRequest(savedAddressId, List.of(orderDetailDto));
        AtomicInteger successCount = new AtomicInteger();

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    // 비즈니스 로직 호출 (주문 생성)
                    orderService.createOrder(request, testUser.getUserId());
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    // 재고 부족 등의 예외가 발생할 경우 에러 기록
                    log.error("주문 실패: {}", e.getMessage());
                } finally {
                    latch.countDown(); // 쓰레드 작업 종료 알림
                }
            });
        }
        latch.await(); // 100개 쓰레드의 실행이 모두 끝날 때까지 대기

        // then: 1차 캐시를 초기화하여 DB 데이터를 새로 SELECT 하도록 유도
        em.clear();
        // 1. DB에서 최신 상태의 ProductDetail 조회
        Item updatedItem = itemRepository.findById(testItem.getItemId())
                .orElseThrow();
        System.out.println("성공한 주문 횟수: " + successCount.get());
        System.out.println("남은 재고: " + updatedItem.getCount());

        // 2. 검증: 초기 재고 100개 - (성공한 주문 100회 * 1개) = 남은 재고 0개여야 함
        assertThat(updatedItem.getCount()).isEqualTo(0);
    }


}