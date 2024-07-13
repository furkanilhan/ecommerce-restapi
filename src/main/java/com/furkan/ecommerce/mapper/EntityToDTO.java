package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.ProductDetailDTO;
import com.furkan.ecommerce.dto.ProductVariantDTO;
import com.furkan.ecommerce.dto.CategoryDTO;
import com.furkan.ecommerce.dto.ProductDTO;
import com.furkan.ecommerce.model.ProductVariant;
import com.furkan.ecommerce.model.Category;
import com.furkan.ecommerce.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityToDTO {
    public ProductDTO toProductDTO(Product product) {
        if (product == null) {
            return null;
        }
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .brand(product.getBrand())
                .model(product.getModel())
                .category(toCategoryDTO(product.getCategory()))
                .build();
    }

    public ProductDetailDTO toProductDetailDTO(Product product) {
        if (product == null) {
            return null;
        }
        return ProductDetailDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .type(product.getType())
                .brand(product.getBrand())
                .model(product.getModel())
                .category(toCategoryDTO(product.getCategory()))
                .productVariantDTOS(toProductVariantDTOs(product.getProductVariants()))
                .build();
    }

    public CategoryDTO toCategoryDTO(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    public List<ProductVariantDTO> toProductVariantDTOs(List<ProductVariant> productVariants) {
        if (productVariants == null) {
            return null;
        }
        return productVariants.stream()
                .map(productVariant -> {
                    ProductVariantDTO dto = new ProductVariantDTO();
                    dto.setId(productVariant.getId());
                    dto.setProductId(productVariant.getProduct().getId());
                    dto.setColor(productVariant.getColor());
                    dto.setVariantKey(productVariant.getVariantKey());
                    dto.setVariantValue(productVariant.getVariantValue());
                    dto.setPrice(productVariant.getPrice());
                    dto.setQuantity(productVariant.getQuantity());

                    return dto;
                })
                .collect(Collectors.toList());
    }
}
