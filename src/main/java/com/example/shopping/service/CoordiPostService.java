package com.example.shopping.service;

import com.example.shopping.dto.CoordiPostDto;
import com.example.shopping.entity.CoordiPost;
import com.example.shopping.entity.User;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import com.example.shopping.repository.CoordiPostRepository;
import com.example.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoordiPostService {

    private final CoordiPostRepository coordiPostRepository;
    private final UserRepository userRepository;
        @Transactional
        public CoordiPostDto.CreateCoordiPostResponse createCoordiPost(Long userId, CoordiPostDto.CreateCoordiPostRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        CoordiPost post = request.toEntity(user);
        CoordiPost saved = coordiPostRepository.save(post);

        return CoordiPostDto.CreateCoordiPostResponse.fromEntity(saved);

    }
        // 최신순, 좋아요 순 정렬
        @Transactional(readOnly = true)
        public CoordiPostDto.SimpleCoordiPostListResponse getAllCoordiPostOrderBy(Long userId,String orderType, int page) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Pageable pageable = PageRequest.of(page, 8);
        Slice<CoordiPostDto.SimpleCoordiPostResponse> coordiPosts= coordiPostRepository.getAllCoordiPostOrderBy(userId, orderType, pageable);
        return new CoordiPostDto.SimpleCoordiPostListResponse(coordiPosts.getContent(), coordiPosts.hasNext());

    }

    @Transactional(readOnly = true)
    public CoordiPostDto.SimpleTop10CoordiPostListResponse getTop10CoordiPost(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        List<CoordiPostDto.SimpleCoordiPostProjection> posts = coordiPostRepository.findTop10ByLikeCountDesc(userId);
        return CoordiPostDto.SimpleTop10CoordiPostListResponse.fromProjection(posts);
    }
}
