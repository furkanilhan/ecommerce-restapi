package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.ProductVariantDTO;
import com.furkan.ecommerce.model.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ColorMapper.class,
        VariantMapper.class})
public interface ProductVariantMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "color", target = "color")
    @Mapping(source = "variant", target = "variant")
    ProductVariantDTO toProductVariantDTO(ProductVariant productVariant);

    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "color", target = "color")
    @Mapping(source = "variant", target = "variant")
    ProductVariant toProductVariant(ProductVariantDTO productVariantDTO);

}
