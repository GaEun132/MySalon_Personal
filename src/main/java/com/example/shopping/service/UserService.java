package com.example.shopping.service;


import com.example.shopping.dto.UserDto;
import com.example.shopping.entity.Favorite;
import com.example.shopping.entity.User;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import com.example.shopping.repository.*;
import com.example.shopping.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final OrderRepository orderRepository;
    private final FavoriteRepository favoriteRepository;
    private final ReviewRepository reviewRepository;
    private final PostRepository postRepository;

    // 유저 생성
    @Transactional
    public UserDto.Response createUser(UserDto.Request request) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        String encodedPaymentPassword = passwordEncoder.encode(request.getPaymentPassword());
        if (userRepository.existsById(request.getId())) {
            throw new BusinessException(ErrorCode.USER_ID_DUPLICATE);
        }
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new BusinessException(ErrorCode.USER_NAME_DUPLICATE);
        }

        User user = User.builder()
                .id(request.getId())
                .password(encodedPassword)
                .userName(request.getUserName())
                .paymentPassword(encodedPaymentPassword)
                .profileImage(request.getProfileImage())
                .gender(request.getGender())
                .tall(request.getTall())
                .weight(request.getWeight())
                .type(request.getType())
                .build();

        return UserDto.Response.fromEntity(userRepository.save(user));
    }

    // 유저 수정
    @Transactional
    public UserDto.Response editUser(Long userId, UserDto.Request request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호가 null이 아니면 암호화 후 업데이트
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.changePassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getPaymentPassword() != null && !request.getPaymentPassword().isEmpty()) {
            user.changePaymentPassword(passwordEncoder.encode(request.getPaymentPassword()));
        }
        // 프로필 이미지
        if (request.getProfileImage() != null && !request.getProfileImage().isEmpty()) {
            user.changeProfileImage(request.getProfileImage());
        }

        // 성별
        if (request.getGender() != null) {
            user.changeGender(request.getGender());
        }

        // 키
        if (request.getTall() != null) {
            user.changeTall(request.getTall());
        }

        // 몸무게
        if (request.getWeight() != null) {
            user.changeWeight(request.getWeight());
        }


        return UserDto.Response.fromEntity(userRepository.save(user));
    }

    // 유저 삭제
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsByUserId(userId)) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        userRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    public List<UserDto.Response> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDto.Response::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDto.Response getUserByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return UserDto.Response.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public UserDto.LoginResponse login(UserDto.LoginRequest authRequest) {
        User user = userRepository.findById(authRequest.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.AUTH_INVALID_CREDENTIALS);
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getId(),
                        authRequest.getPassword()
                ));
        if (authentication.isAuthenticated()) {
            String token =  jwtService.generateToken(authRequest.getId());
            return UserDto.LoginResponse.builder()
                    .token(token)
                    .role(user.getType())
                    .userName(user.getUserName())
                    .build();
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }

    }

    @Transactional(readOnly = true)
    public UserDto.GetMyPageResponse getMyPageInfo(Long userId) {
        // 주문 내역, 찜, 리뷰, 개시물 개수를 반환
        Long orderCount = orderRepository.countAllByUserUserId(userId);
        Long favoriteCount = favoriteRepository.countAllByUserUserId(userId);
        Long reviewCount = reviewRepository.countAllByUserUserId(userId);
        Long postCount = postRepository.countAllByUserUserId(userId);
        return new UserDto.GetMyPageResponse(orderCount, favoriteCount, reviewCount, postCount);

    }
}