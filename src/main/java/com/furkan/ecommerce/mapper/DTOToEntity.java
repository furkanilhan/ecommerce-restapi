package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.CategoryDTO;
import com.furkan.ecommerce.dto.ProductDetailDTO;
import com.furkan.ecommerce.dto.ProductVariantDTO;
import com.furkan.ecommerce.model.Product;
import com.furkan.ecommerce.model.Category;
import com.furkan.ecommerce.model.ProductVariant;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DTOToEntity {
    public Product toProduct(ProductDetailDTO productDetailDTO) {
        if (productDetailDTO == null) {
            return null;
        }

        Product product = new Product();
        product.setId(productDetailDTO.getId());
        product.setName(productDetailDTO.getName());
        product.setDescription(productDetailDTO.getDescription());
        product.setType(productDetailDTO.getType());
        product.setBrand(productDetailDTO.getBrand());
        product.setModel(productDetailDTO.getModel());
        product.setCategory(toCategory(productDetailDTO.getCategory()));
        product.setProductVariants(toProductVariants(productDetailDTO.getProductVariants(), product));

        return product;
    }

    public Category toCategory(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }

        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        return category;
    }

    public List<ProductVariant> toProductVariants(List<ProductVariantDTO> productVariantDTOs, Product product) {
        if (productVariantDTOs == null) {
            return Collections.emptyList();
        }

        return productVariantDTOs.stream()
                .map(dto -> {
                    ProductVariant productVariant = new ProductVariant();
                    productVariant.setProduct(product);
                    productVariant.setColor(dto.getColor());
                    productVariant.setVariantKey(dto.getVariantKey());
                    productVariant.setVariantValue(dto.getVariantValue());
                    productVariant.setPrice(dto.getPrice());
                    productVariant.setQuantity(dto.getQuantity());

                    return productVariant;
                })
                .collect(Collectors.toList());
    }
}
