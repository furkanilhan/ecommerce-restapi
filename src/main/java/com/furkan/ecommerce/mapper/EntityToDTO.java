package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.*;
import com.furkan.ecommerce.model.Color;
import com.furkan.ecommerce.model.ProductVariant;
import com.furkan.ecommerce.model.Category;
import com.furkan.ecommerce.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EntityToDTO {

    @Mapping(target = "category", source = "product.category")
    ProductDTO toProductDTO(Product product);

    @Mapping(target = "productVariants", source = "product.productVariants")
    ProductDetailDTO toProductDetailDTO(Product product);

    CategoryDTO toCategoryDTO(Category category);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "color", source = "color")
    ProductVariantDTO toProductVariantDTO(ProductVariant productVariant);

    List<ProductVariantDTO> toProductVariantDTOs(List<ProductVariant> productVariants);

    ColorDTO toColorDTO(Color color);
}