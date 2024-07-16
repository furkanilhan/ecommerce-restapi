package com.furkan.ecommerce.service;

import com.furkan.ecommerce.dto.ProductVariantDTO;
import com.furkan.ecommerce.dto.ProductVariantFilterDTO;
import com.furkan.ecommerce.model.*;

import java.util.List;

public interface ProductVariantService {

    List<ProductVariantDTO> filterProductVariants(ProductVariantFilterDTO filterDTO);

    void decreaseProductVariantsQuantity(List<OrderItem> orderItems);

    void increaseProductVariantsQuantity(List<OrderItem> orderItems);
}
