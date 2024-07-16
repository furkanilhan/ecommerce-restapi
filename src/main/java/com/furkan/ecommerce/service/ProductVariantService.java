package com.furkan.ecommerce.service;

import com.furkan.ecommerce.dto.ProductVariantDTO;
import com.furkan.ecommerce.model.*;

import java.math.BigDecimal;
import java.util.List;

public interface ProductVariantService {

    List<ProductVariantDTO> filterProductVariants(Integer minQuantity, Integer maxQuantity,
                                                  BigDecimal minPrice, BigDecimal maxPrice,
                                                  Long colorId, Long variantId,
                                                  Long productTypeId, Long brandId, Long brandModelId);

    void deceraseProductVariantsQuantity(List<OrderItem> orderItems);

    void increaseProductVariantsQuantity(List<OrderItem> orderItems);
}
