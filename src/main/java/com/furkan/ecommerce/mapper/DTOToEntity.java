package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.*;
import com.furkan.ecommerce.model.*;
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

    Variant toVariant(VariantDTO variantDTO);

    @Mappings({
            @Mapping(target = "product", ignore = true),
            @Mapping(target = "color", source = "color"),
            @Mapping(target = "variant", source = "variant")
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
