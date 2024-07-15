package com.furkan.ecommerce.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AvailableQuantityResponse {
    private Long productVariantId;
    private Integer quantity;
}
