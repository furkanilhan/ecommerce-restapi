package com.furkan.ecommerce.service;

import com.furkan.ecommerce.dto.ProductVariantDTO;
import com.furkan.ecommerce.dto.ProductVariantFilterDTO;
import com.furkan.ecommerce.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductVariantService {

    ProductVariantDTO getProductVariantById(Long id);

    Page<ProductVariantDTO> filterProductVariants(ProductVariantFilterDTO filterDTO, Pageable pageable);

    void decreaseProductVariantsQuantity(List<OrderItem> orderItems);

    void increaseProductVariantsQuantity(List<OrderItem> orderItems);
}
