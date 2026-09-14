package com.example.shopping.repository;

import com.example.shopping.dto.CoordiPostDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CoordiPostRepositoryCustom {
    Slice<CoordiPostDto.SimpleCoordiPostResponse> getAllCoordiPostOrderBy(Long userId, String sortType, Pageable pageable);
}
