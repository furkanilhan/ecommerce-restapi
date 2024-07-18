package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.VariantDTO;
import com.furkan.ecommerce.model.Variant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VariantMapper {

    VariantDTO toVariantDTO(Variant variant);

    Variant toVariant(VariantDTO variantDTO);
}
