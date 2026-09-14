package com.example.shopping.service;


import com.example.shopping.dto.CoordiPostDto;
import com.example.shopping.entity.*;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import com.example.shopping.repository.CoordiPostLikeRepository;
import com.example.shopping.repository.CoordiPostRepository;
import com.example.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class CoordiPostLikeService {

    private final CoordiPostLikeRepository coordiPostLikeRepository;
    private final UserRepository userRepository;
    private final CoordiPostRepository coordiPostRepository;


    @Transactional
    public CoordiPostDto.ClickCoordiPostLikeResponse clickPost(Long userId, Long coordiPostId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        CoordiPost post = coordiPostRepository.findById(coordiPostId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COORDI_POST_NOT_FOUND));

        if (coordiPostLikeRepository.existsByUserUserIdAndCoordiPostCoordiPostId(userId,coordiPostId)) {
            coordiPostLikeRepository.deleteByUserUserIdAndCoordiPostCoordiPostId(userId,coordiPostId);
            post.decreaseLikeCount();
            coordiPostRepository.save(post);
            return CoordiPostDto.ClickCoordiPostLikeResponse.fromEntity(post, false);

        } else {
            post.increaseLikeCount();
            CoordiPostLike coordiPostLike = CoordiPostLike.builder()
                    .user(user)
                    .coordiPost(post)
                    .createdAt(LocalDateTime.now())
                    .build();
            coordiPostLikeRepository.save(coordiPostLike);
            coordiPostRepository.save(post);
            return CoordiPostDto.ClickCoordiPostLikeResponse.fromEntity(post, true);
        }

    }

}
