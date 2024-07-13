package com.furkan.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductVariantDTO {
    private Long id;
    private Long productId;
    private String color;
    private String variantKey;
    private String variantValue;
    private Integer quantity;
    private BigDecimal price;
}
