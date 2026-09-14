package com.example.shopping.controller;

import com.example.shopping.dto.ItemDto;
import com.example.shopping.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto.GetItemResponse> getItemInfoById(Long itemId) {
        ItemDto.GetItemResponse dto = itemService.getItemInfoById(itemId);
        return ResponseEntity.ok(dto);
    }
}
