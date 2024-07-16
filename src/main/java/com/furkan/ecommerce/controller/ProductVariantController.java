package com.furkan.ecommerce.controller;

import com.furkan.ecommerce.dto.ProductVariantDTO;
import com.furkan.ecommerce.service.ProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/productVariant")
public class ProductVariantController {

    @Autowired
    private ProductVariantService productVariantService;

    @GetMapping("/filter")
    public List<ProductVariantDTO> filterProductVariants(@RequestParam(required = false) Integer minQuantity,
                                                         @RequestParam(required = false) Integer maxQuantity,
                                                         @RequestParam(required = false) BigDecimal minPrice,
                                                         @RequestParam(required = false) BigDecimal maxPrice,
                                                         @RequestParam(required = false) Long colorId,
                                                         @RequestParam(required = false) Long variantId,
                                                         @RequestParam(required = false) Long productTypeId,
                                                         @RequestParam(required = false) Long brandId,
                                                         @RequestParam(required = false) Long brandModelId
                                                         ) {
        return productVariantService.filterProductVariants(minQuantity, maxQuantity, minPrice,
                maxPrice, colorId, variantId, productTypeId, brandId, brandModelId);
    }
}
