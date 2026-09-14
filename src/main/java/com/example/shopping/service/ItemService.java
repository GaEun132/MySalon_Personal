package com.example.shopping.service;

import com.example.shopping.dto.ItemDto;
import com.example.shopping.entity.Item;
import com.example.shopping.exception.BusinessException;
import com.example.shopping.exception.ErrorCode;
import com.example.shopping.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    @Transactional
    public ItemDto.GetItemResponse getItemInfoById(Long itemId) {
        Item item = itemRepository.findItemInfoById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));
        return ItemDto.GetItemResponse.fromEntity(item);
    }
}
