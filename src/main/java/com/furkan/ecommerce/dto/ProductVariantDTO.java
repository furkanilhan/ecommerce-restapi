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
    private ColorDTO color;
    private VariantDTO variant;
    private Integer quantity;
    private BigDecimal price;
}
