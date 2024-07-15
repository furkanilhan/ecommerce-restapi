package com.furkan.ecommerce.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDTO {
    private Long id;
    private String name;
    private BrandDTO brand;
    private BrandModelDTO brandModel;
    private CategoryDTO category;
    private String description;
    private ProductTypeDTO productType;
    private List<ProductVariantDTO> productVariants;
}
