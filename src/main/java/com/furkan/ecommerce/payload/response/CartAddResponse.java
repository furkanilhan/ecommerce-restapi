package com.furkan.ecommerce.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CartAddResponse {
    private List<Long> successfulAddedProducts;
    private List<Long> failedToAdd;
    private List<AvailableQuantityResponse> availableStock;
}
