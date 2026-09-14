package com.example.shopping.service;


import com.example.shopping.dto.CommentDto;
import com.example.shopping.entity.Comment;
import com.example.shopping.entity.Post;
import com.example.shopping.entity.User;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import com.example.shopping.repository.CommentRepository;
import com.example.shopping.repository.PostRepository;
import com.example.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentDto.CreateCommentResponse createComment(Long userId, CommentDto.CreateCommentRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(request.getPostId()).orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        Comment comment = request.toEntity(user, post);
        Comment saved = commentRepository.save(comment);
        return CommentDto.CreateCommentResponse.fromEntity(saved);
    }
    @Transactional
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
        }
        commentRepository.deleteById(commentId);

    }
    @Transactional(readOnly = true)
    public CommentDto.GetUserCommentListResponse getUserComment(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 3);
        Slice<Comment> comments = commentRepository.findByUserUserId(userId, pageable);
        return CommentDto.GetUserCommentListResponse.fromEntity(comments);

    }
}
