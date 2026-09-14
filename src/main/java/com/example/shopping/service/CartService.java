package com.example.shopping.service;

import com.example.shopping.dto.CartDto;
import com.example.shopping.entity.Cart;
import com.example.shopping.entity.Item;
import com.example.shopping.entity.User;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import com.example.shopping.repository.CartRepository;
import com.example.shopping.repository.ItemRepository;
import com.example.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    @Transactional(readOnly = true)
    public CartDto.GetUserCartListResponse getUserCart(Long userId, int page) {

        Pageable pageable = PageRequest.of(page, 5);
        Slice<Cart> carts = cartRepository.findByUserUserIdOrderByCreatedAtDes(userId, pageable);
        if (carts.isEmpty()) {
            return null;
        }
        return CartDto.GetUserCartListResponse.fromEntity(carts);

    }
    @Transactional
    public CartDto.CreateCartResponse createCart(Long userId, CartDto.CreateCartRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));
        if (cartRepository.existsByUserUserIdAndItemItemId(userId, request.getItemId())) {
            throw new BusinessException(ErrorCode.CART_ALREADY_EXISTS);
        }

        Cart cart = request.toEntity(item, user, request.getQuantity());
        Cart savedCart = cartRepository.save(cart);

        return CartDto.CreateCartResponse.fromEntity(savedCart);
    }
    @Transactional
    public void deleteCart(Long userId, Long cartId) {
        cartRepository.deleteByUserUserIdAndCartId(userId, cartId);
    }
    @Transactional
    public CartDto.UpdateIsSelectedResponse updateIsSelected(Long userId, Long cartId) {
        Cart cart = cartRepository.findByUserUserIdAndCartId(userId, cartId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_NOT_FOUND));
        cart.changeIsSelected();
        return CartDto.UpdateIsSelectedResponse.fromEntity(cart);
    }


    public CartDto.GetUserCartListResponse getUserSelectedCart(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Slice<Cart> selectedCarts = cartRepository.findAllByUserUserIdAndIsSelectedTrue(userId, pageable);
        if (selectedCarts.isEmpty()) {
            return null;
        }
        return CartDto.GetUserCartListResponse.fromEntity(selectedCarts);
    }

    @Transactional
    public void deleteCartAfterOrder(Long userId, CartDto.DeleteCartAfterOrderRequest request) {
        List<Long> cartIds = request.getCartIds();
        cartRepository.deleteAllByUserUserIdAndCartIdIn(userId, cartIds);
    }
}
