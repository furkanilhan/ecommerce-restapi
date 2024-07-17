package com.furkan.ecommerce.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantDTO {
    private Long id;
    private ProductDTO product;
    private ColorDTO color;
    private VariantDTO variant;
    private Integer quantity;
    private Integer reservedQuantity;
    private BigDecimal price;
}
