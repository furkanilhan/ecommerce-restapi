package com.furkan.ecommerce.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private BrandDTO brand;
    private BrandModelDTO brandModel;
    private CategoryDTO category;
}
