package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.*;
import com.furkan.ecommerce.model.Product;
import com.furkan.ecommerce.model.Category;
import com.furkan.ecommerce.model.Color;
import com.furkan.ecommerce.model.ProductVariant;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DTOToEntity {

    @Mappings({
            @Mapping(target = "category", source = "category"),
            @Mapping(target = "productVariants", source = "productVariants")
    })
    Product toProduct(ProductDetailDTO productDetailDTO);

    Category toCategory(CategoryDTO categoryDTO);

    Color toColor(ColorDTO colorDTO);

    @Mappings({
            @Mapping(target = "product", ignore = true),
            @Mapping(target = "color", source = "color")
    })
    ProductVariant toProductVariant(ProductVariantDTO productVariantDTO);

    List<ProductVariant> toProductVariants(List<ProductVariantDTO> productVariantDTOs);

    @AfterMapping
    default void linkProduct(@MappingTarget Product product) {
        if (product.getProductVariants() != null) {
            product.getProductVariants().forEach(variant -> variant.setProduct(product));
        }
    }
}
