package com.furkan.ecommerce.controller;

import com.furkan.ecommerce.dto.ProductVariantDTO;
import com.furkan.ecommerce.dto.ProductVariantFilterDTO;
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
    public List<ProductVariantDTO> filterProductVariants(
            @RequestParam(required = false) Integer minQuantity,
            @RequestParam(required = false) Integer maxQuantity,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) List<Long> colorIds,
            @RequestParam(required = false) List<Long> variantIds,
            @RequestParam(required = false) List<Long> productTypeIds,
            @RequestParam(required = false) List<Long> brandIds,
            @RequestParam(required = false) List<Long> brandModelIds) {

        ProductVariantFilterDTO filterDTO = new ProductVariantFilterDTO();
        filterDTO.setMinQuantity(minQuantity);
        filterDTO.setMaxQuantity(maxQuantity);
        filterDTO.setMinPrice(minPrice);
        filterDTO.setMaxPrice(maxPrice);
        filterDTO.setColorIds(colorIds);
        filterDTO.setVariantIds(variantIds);
        filterDTO.setProductTypeIds(productTypeIds);
        filterDTO.setBrandIds(brandIds);
        filterDTO.setBrandModelIds(brandModelIds);

        return productVariantService.filterProductVariants(filterDTO);
    }

    //TODO: productVariant ekleme
    //TODO: productVariant silme
    //TODO: productVariant güncelleme
}
