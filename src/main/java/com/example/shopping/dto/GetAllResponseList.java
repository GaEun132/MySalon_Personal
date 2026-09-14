package com.example.shopping.dto;

import com.example.shopping.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetAllResponseList {
    private List<GetAllResponse> products;
    private boolean hasNext;

    public GetAllResponseList fromGetAllResponse(Slice<GetAllResponse> products) {
        return GetAllResponseList.builder()
                .products(products.getContent())
                .hasNext(products.hasNext())
                .build();
    }


    public GetAllResponseList fromProjections(Slice<ProductDto.ProductListProjection> projections) {
        List<GetAllResponse> products = projections.stream()
                .map(GetAllResponse::fromProjection)
                .toList();
        return GetAllResponseList.builder()
                .products(products)
                .hasNext(projections.hasNext())
                .build();
    }
}
