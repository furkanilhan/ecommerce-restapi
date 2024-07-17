package com.furkan.ecommerce.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private Long id;
    private ProductVariantDTO productVariant;
    private String productName;
    private int quantity;
    private BigDecimal price;

}
