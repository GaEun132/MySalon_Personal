package com.example.shopping.service;


import com.example.shopping.dto.OrderDto;
import com.example.shopping.entity.*;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import com.example.shopping.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final AddressRepository addressRepository;
    private final DeliveryRepository deliveryRepository;



    // 주문 생성
    @Transactional
    public OrderDto.OrderCompleteResponse createOrder(OrderDto.CreateOrderRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // OrderDetail 생성 (count, order, productDetail)
        // 1. DTO 리스트를 [orderDetail_ID : 주문수량] 형태의 Map으로 변환
        Map<Long, Integer> productCountMap = request.getOrderDetails().stream()
                .collect(Collectors.toMap(
                        OrderDto.OrderDetailDto::getItemId,
                        OrderDto.OrderDetailDto::getCount
                ));
        List<Long> productIds = new ArrayList<>(productCountMap.keySet());
        // 데드락 방지: 스레드1: [1,2] 스레드 2: [2,1] -> 스레드1이 1번 락을 잡고 스레드2가 2번 락을 잡으면 서로 기다리게 됨
        Collections.sort(productIds);

        // 2. 상품 ID 리스트를 이용해 DB에서 ProductDetail 엔티티들을 한 번에 조회
        // 동시성 문제를 방지하기 위해 비관적 락을 사용하여 조회
        List<Item> Item = itemRepository.findAllByIdWithPessimisticLock(productIds);
        //List<ProductDetail> productDetails = productDetailRepository.findAllById(productIds);

        // 요청된 상품과 DB 조회 결과 수량 일치 검증
        if (Item.size() != productIds.size()) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        // 3. OrderDetail 생성
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (Item item : Item) {
            // 주문 수량 추출
            int count = productCountMap.get(item.getItemId());
            // 주문상품 생성
            OrderDetail orderDetail = OrderDetail.createOrderDetail(item, item.getProduct().getPrice(), count);
            orderDetails.add(orderDetail);
        }
        // 데드락 방지
        //productDetailRepository.saveAllAndFlush(productDetails);

        // 4. Delivery 생성
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ADDRESS_NOT_FOUND));
        Delivery delivery =  Delivery.createDelivery(address);
        deliveryRepository.save(delivery);


        // 주문 생성(user, delivery, orderDetail, orderedAt)
        Order order = Order.createOrder(user,delivery, orderDetails.toArray(new OrderDetail[0]));
        Order savedOrder = orderRepository.save(order);
        return OrderDto.OrderCompleteResponse.fromEntity(savedOrder);
    }
    @Transactional(readOnly = true)
    public OrderDto.OrderHistoryListResponse getOrderHistoryBefore(Long userId, int page) {
        // 페이징: 1페이지당 5개, 주문일자 내림차순 정렬
        Pageable pageable = PageRequest.of(page, 5, Sort.by("orderedAt").descending());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        // 1. 유저 id로 Order 5건 조회
        Page<Order> orders = orderRepository.findByUserUserId(userId, pageable);
        // 2. order의 pk 추출
        List<Long> orderNums = orders.stream()
                .map(Order::getOrderId)
                .collect(Collectors.toList());

        // findAllOrderDetailsByOrderNums()을 사용하는 경우
        // 1. 5개 주문에 연관된 모든 상세 내역을 한 방에 긁어옴 (N+1 없음)
        List<OrderDetail> allDetails = orderRepository.findAllOrderDetailsByOrderIds(orderNums);

        // 2. 주문 번호별로 그룹화한 뒤, 자바 코드로 '최대 2개'만 제한(limit)하여 맵에 저장
        Map<Long, List<OrderDetail>> detailsMap = allDetails.stream()
                .collect(Collectors.groupingBy(
                        od -> od.getOrder().getOrderId(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream().limit(2).collect(Collectors.toList()) // 💡 여기서 2개 제한!
                        )
                ));
        // 4. DTO 반환
        return OrderDto.OrderHistoryListResponse.fromEntity(orders,detailsMap);
    }

    // 주문 내역 조회
    // Product의 상품명, 가격 조회
    // ProductDetail의 사이즈, 색상, 배송상태 조회
    // OrderDetail의 pk, 주문 수량 조회
    // Order의 pk, 주문 일자, 주문 번호 조회
    // 1. 유저 id로 List<Order> 조회
    // 2. 각 Order당 연관된 OrderDetail 2개 조회(연관된 주문상세 리스트를 조회하는 쿼리 발생)
    // 3. 각 OrderDetail당 ProductDetail 조회 (지연로딩, @BatchSize로 최적화)
    // 4. 각 ProductDetail당 Product 조회 (지연로딩, @BatchSize로 최적화)
    @Transactional
    public OrderDto.OrderHistoryListResponse getOrderHistory(Long userId, int page) {
        // 페이징: 1페이지당 5개, 주문일자 내림차순 정렬
        Pageable pageable = PageRequest.of(page, 5, Sort.by("orderedAt").descending());
        // 1. 유저 id로 Order 5건 조회
        Page<Order> orders = orderRepository.findByUserUserId(userId, pageable);
        /*
        select  o1_0.order_num,
                o1_0.ordered_at,
                o1_0.user_num
        from
        orders o1_0

        where
        o1_0.user_num=?

        order by
        o1_0.ordered_at desc

        limit ?, ?
        --------------------
            select
        count(*)
    from
        orders o1_0
    where
        o1_0.user_num=?
        */
        // 2. order의 pk 추출
        List<Long> orderNums = orders.stream()
                .map(Order::getOrderId)
                .collect(Collectors.toList());

        // order pk에 해당하는 orderDetails 상위 2건 조회
        List<OrderDetail> top2OrderDetails = orderRepository.findTop2OrderDetailsByOrderIds(orderNums);
        /*
        SELECT
                *
                FROM
                        (     SELECT
                                od.*,
                                ROW_NUMBER() OVER (PARTITION
                                        BY
                                        od.order_num
                                        ORDER BY
                                        od.order_detail_num ASC) as rn
                                FROM
                                order_details od
                                WHERE
                                od.order_num IN (?, ?, ?, ?, ?) ) ranked
                WHERE
        ranked.rn <= 2
        */

        // 3. 주문 ID별로 그룹화 (Map<OrderNum, List<OrderDetail>>)
        Map<Long, List<OrderDetail>> detailsMap = top2OrderDetails.stream()
                .collect(Collectors.groupingBy(od -> od.getOrder().getOrderId()));
         /*
            select
        pd1_0.product_detail_num,
        pd1_0.color,
        pd1_0.count,
        pd1_0.image,
        pd1_0.product_num,
        pd1_0.size
    from
        product_details pd1_0
    where
        pd1_0.product_detail_num in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
Hibernate:
    select
        p1_0.product_num,
        p1_0.category,
        p1_0.category_low,
        p1_0.delivery_price,
        p1_0.description,
        p1_0.gender,
        p1_0.like_count,
        p1_0.main_image,
        p1_0.price,
        p1_0.product_name,
        p1_0.user_num
    from
        products p1_0
    where
        p1_0.product_num in (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        */

        // 4. DTO 반환
        return OrderDto.OrderHistoryListResponse.fromEntity(orders,detailsMap);
    }



}
