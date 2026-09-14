package com.example.shopping.controller;


import com.example.shopping.dto.GetAllResponseList;
import com.example.shopping.dto.ProductDto;
import com.example.shopping.security.CurrentUser;
import com.example.shopping.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<ProductDto.CreateProductResponse> createProduct(
            @CurrentUser Long userId,
            @RequestBody ProductDto.CreateProductRequest request) {

        return ResponseEntity.ok(productService.createProduct(userId, request));
    }

    @PatchMapping(value = "/{productId}")
    public ResponseEntity<ProductDto.UpdateProductResponse> updateProduct(@CurrentUser Long userId,@PathVariable Long productId, @RequestBody ProductDto.UpdateProductRequest request) {
        return ResponseEntity.ok(productService.editProduct(userId, productId, request));
    }

    @GetMapping("/join")
    public ResponseEntity<GetAllResponseList> getAllProductsByJoin(@CurrentUser Long userNum, @RequestParam int page) {
        return ResponseEntity.ok(productService.getAllProductsByJoin(userNum, page));
    }
    @GetMapping("/multi-query")
    public ResponseEntity<GetAllResponseList> getAllProductsByMultiQuery(@CurrentUser Long userNum, @RequestParam int page) {
        return ResponseEntity.ok(productService.getAllProductsByMultiQuery(userNum, page));
    }

    @GetMapping("/tuning")
    public ResponseEntity<GetAllResponseList> getAllProductsByTuning(@CurrentUser Long userNum, @RequestParam int page) {
        return ResponseEntity.ok(productService.getAllProductsByTuning(userNum, page));
    }

    @GetMapping("{productId}")
    public ResponseEntity<ProductDto.GetProductItemResponse> getProductItemById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductItemById(productId));
    }
    @PostMapping("/{categoryId}/order-by/sales-count")
    public ResponseEntity<GetAllResponseList> getAllProductsByCategoryIdOrderBySalesCount(@CurrentUser Long userId,@PathVariable Long categoryId, @RequestParam int page) {
        return ResponseEntity.ok(productService.getAllProductsByCategoryIdOrderBySalesCount(userId, categoryId, page));
    }
    @PostMapping("/{categoryId}/order-by")
    public ResponseEntity<GetAllResponseList> getAllProductsByCategoryIdOrderBy(@CurrentUser Long userId,@PathVariable Long categoryId,@RequestBody ProductDto.ProductSearchCondition condition, @RequestParam int page) {
        return ResponseEntity.ok(productService.getAllProductsByCategoryIdOrderBy(userId, categoryId,condition, page));
    }

    




}