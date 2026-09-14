package com.example.shopping.service;

import com.example.shopping.dto.GetAllResponse;
import com.example.shopping.dto.GetAllResponseList;
import com.example.shopping.dto.ProductDto;
import com.example.shopping.entity.*;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import com.example.shopping.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    @Transactional
    public ProductDto.CreateProductResponse createProduct(Long userId, ProductDto.CreateProductRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

/*        // 메인 이미지 저장
        String originalFileName = null;
        if (mainImageFile != null && !mainImageFile.isEmpty()) {
            try {
                // 고유한 파일명 생성 (예: UUID 사용)
                originalFileName = UUID.randomUUID().toString() + "_" + mainImageFile.getOriginalFilename();
                String uploadDir = "C:/uploads/";
                Path filePath = Paths.get(uploadDir + originalFileName);

                // 파일 저장
                Files.copy(mainImageFile.getInputStream(), filePath);
            } catch (java.io.IOException e) {
                throw new RuntimeException("상품 이미지 업로드 실패", e);
            }
        }
        // item의 개별 이미지 저장*/
        // toEntity 메서드에 파일 이름 전달
        List<Item> items = request.getItems().stream()
                .map(ProductDto.CreateItemRequest::toEntity)
                .toList();
        Product product = request.toEntity(user, category, items.toArray(new Item[0]));
        Product savedProduct = productRepository.save(product);

        return ProductDto.CreateProductResponse.fromEntity(savedProduct);
    }
    @Transactional
    public ProductDto.UpdateProductResponse editProduct(Long userId, Long productId, ProductDto.UpdateProductRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        User changeUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Category changeCategory = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        // 아이템들을 맵으로 변환 (Key: itemId, Value: Item)
        Map<Long, Item> currentItemMap = product.getItems().stream()
                .collect(Collectors.toMap(Item::getItemId, item -> item));

        for (ProductDto.UpdateReviewRequest dto : request.getItems()) {
            if (dto.getItemId() == null) {
                // 새로운 아이템 추가
                product.assignItem(dto.toEntity());

            } else {
                Item existing = currentItemMap.remove(dto.getItemId());
                if (existing == null) {
                    throw new BusinessException(ErrorCode.ITEM_NOT_FOUND);
                }
                product.changeItem(dto.getItemId(), dto.getSize(), dto.getColor(), dto.getCount(), dto.getImage());
            }
        }
        currentItemMap.values().forEach(product::removeItem);

        if (request.getProductName()!= null) {
            product.changeName(request.getProductName());

        }
        if (request.getDeliveryFee()!= null) {
            product.changeDeliveryPrice(request.getDeliveryFee());
        }
        if (request.getUserId()!= null) {
            product.changeUser(changeUser);
        }
        if (request.getCategoryId()!= null) {
            product.changeCategory(changeCategory);
        }
        if (request.getPrice()!=null) {
            product.changePrice(request.getPrice());
        }
        if (request.getDescription()!=null) {
            product.changeDescription(request.getDescription());
        }
        productRepository.flush();
        return ProductDto.UpdateProductResponse.fromEntity(product);



    }


    @Transactional
    public GetAllResponseList getAllProductsByJoin(Long userId, int page) {
        Pageable limitEight = PageRequest.of(page, 8);
        Slice<GetAllResponse> products = productRepository.findTop8ByAvgScoreByJoin(limitEight, userId);
        return new GetAllResponseList().fromGetAllResponse(products);
    }
    @Transactional
    public GetAllResponseList getAllProductsByMultiQuery(Long userId,int page) {
        Pageable limitEight = PageRequest.of(page, 8);
        Slice<Long> productIds = productRepository.findTop8ProductIds(limitEight);
        List<Long> ids = productIds.getContent();
        List<GetAllResponse> details = productRepository.findProductsDetailsByIds(ids, userId);

// 1번째 쿼리의 ID 순서(productIds)대로 2번째 쿼리 결과(details)의 순서를 재정렬
        List<GetAllResponse> sortedDetails = productIds.stream()
                .map(id -> details.stream()
                        .filter(d -> d.getProductId().equals(id))
                        .findFirst()
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
        return new GetAllResponseList(sortedDetails, productIds.hasNext());
    }
    // 리뷰 평점이 높은 순으로 조회
    // 상품 이미지, 상품명, 가격, 평점, 리뷰 개수, 찜여부
    @Transactional
    public GetAllResponseList getAllProductsByTuning(Long userNum,int page) {
        int offset = page * 8;

        Slice<ProductDto.ProductListProjection> projections =
                productRepository.findProductListWithMetrics(userNum, 8,offset);

        return new GetAllResponseList().fromProjections(projections);
    }

    @Transactional
    public ProductDto.GetProductItemResponse getProductItemById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
        return ProductDto.GetProductItemResponse.fromEntity(product);

    }

    @Transactional(readOnly = true)
    public GetAllResponseList getAllProductsByCategoryIdOrderBySalesCount(Long userId, Long categoryId, int page)  {
        int offset = page * 8;
        List<Long> categoryIds = categoryRepository.findAllChildCategoryIdsByParentId(categoryId);
        Slice<ProductDto.ProductListProjection> projections =
                productRepository.findProductListOrderBySalesCount(userId, categoryIds, 8, offset);
        return new GetAllResponseList().fromProjections(projections);
    }
    @Transactional(readOnly = true)
    public GetAllResponseList getAllProductsByCategoryIdOrderBy(Long userId, Long categoryId,ProductDto.ProductSearchCondition condition, int page) {
        Pageable pageable = PageRequest.of(page, 8);
        List<Long> categoryIds = categoryRepository.findAllChildCategoryIdsByParentId(categoryId);
        Slice<GetAllResponse> products =
                productRepository.findAllProductByCategoryIdOrderBy(userId, categoryIds, condition, pageable);
        return new GetAllResponseList().fromGetAllResponse(products);
    }
}