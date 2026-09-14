package com.example.shopping.service;

import com.example.shopping.dto.CommentDto;
import com.example.shopping.dto.PostDto;
import com.example.shopping.entity.Comment;
import com.example.shopping.entity.Post;
import com.example.shopping.entity.User;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import com.example.shopping.repository.PostRepository;
import com.example.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostDto.CreatePostResponse createPost(Long userId, PostDto.CreatePostRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Post post =  request.toEntity(user);
        Post saved = postRepository.save(post);
        return PostDto.CreatePostResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public PostDto.GetSimplePostListResponse getAllSimplePost(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Post> posts =  postRepository.findAllOrderByCreatedAtDesc(pageable);
        return PostDto.GetSimplePostListResponse.fromEntity(posts);


    }
    @Transactional(readOnly = true)
    public PostDto.PostDetailResponse getPostDetail(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
        return PostDto.PostDetailResponse.fromEntity(post);
    }
    @Transactional
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND);
        }
        postRepository.deleteById(postId);
    }

    @Transactional(readOnly = true)
    public PostDto.GetSimplePostListSliceResponse getUserPost(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 3);
        Slice<Post> posts = postRepository.findByUserUserId(userId, pageable);
        return PostDto.GetSimplePostListSliceResponse.fromEntity(posts);

    }
}
