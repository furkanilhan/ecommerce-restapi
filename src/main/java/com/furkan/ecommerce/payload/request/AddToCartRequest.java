package com.furkan.ecommerce.payload.request;

import lombok.Data;

@Data
public class AddToCartRequest {

    private Long productVariantId;
    private int quantity;

}
