package com.furkan.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantFilterDTO {
    private Integer minQuantity;
    private Integer maxQuantity;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<Long> colorIds;
    private List<Long> variantIds;
    private List<Long> productTypeIds;
    private List<Long> brandIds;
    private List<Long> brandModelIds;
}
