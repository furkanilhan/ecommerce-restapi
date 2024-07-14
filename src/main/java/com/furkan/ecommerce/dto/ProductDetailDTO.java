package com.furkan.ecommerce.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDetailDTO {
    private Long id;
    private String name;
    private String brand;
    private String model;
    private CategoryDTO category;
    private String description;
    private String type;
    private List<ProductVariantDTO> productVariants;
}
