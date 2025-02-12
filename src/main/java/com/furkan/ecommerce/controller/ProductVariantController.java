package com.furkan.ecommerce.controller;

import com.furkan.ecommerce.dto.ProductDetailDTO;
import com.furkan.ecommerce.dto.ProductVariantDTO;
import com.furkan.ecommerce.dto.ProductVariantFilterDTO;
import com.furkan.ecommerce.payload.response.MessageResponse;
import com.furkan.ecommerce.service.ProductVariantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/productVariant")
public class ProductVariantController {

    @Autowired
    private ProductVariantService productVariantService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductVariantDTO> getProductVariantById(@PathVariable Long id) {
        ProductVariantDTO productVariantDTO = productVariantService.getProductVariantById(id);
        return ResponseEntity.ok(productVariantDTO);
    }

    @GetMapping("/filter")
    public Page<ProductVariantDTO> filterProductVariants(
            @RequestParam(required = false) Integer minQuantity,
            @RequestParam(required = false) Integer maxQuantity,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) List<Long> colorIds,
            @RequestParam(required = false) List<Long> variantIds,
            @RequestParam(required = false) List<Long> productTypeIds,
            @RequestParam(required = false) List<Long> brandIds,
            @RequestParam(required = false) List<Long> brandModelIds, Pageable pageable) {

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

        return productVariantService.filterProductVariants(filterDTO, pageable);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductVariantDTO> addProductVariant(@Valid @RequestBody ProductVariantDTO productVariantDTO) {
        ProductVariantDTO savedProductVariant = productVariantService.addProductVariant(productVariantDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProductVariant);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductVariantDTO> updateProductVariant(@Valid @PathVariable Long id, @RequestBody ProductVariantDTO productVariantDTO) {
        ProductVariantDTO savedProduct = productVariantService.updateProductVariant(id, productVariantDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MessageResponse> deleteProductVariant(@Valid @PathVariable Long id) {
        productVariantService.deleteProductVariant(id);
        return ResponseEntity.ok(new MessageResponse("Product variant deleted successfully"));
    }
}
