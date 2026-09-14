package com.example.shopping.repository;

import com.example.shopping.dto.GetAllResponse;
import com.example.shopping.dto.ProductDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ProductRepositoryCustom {
    Slice<GetAllResponse> findAllProductByCategoryIdOrderBy(Long userId, List<Long> categoryIds, ProductDto.ProductSearchCondition condition, Pageable pageable);
}
