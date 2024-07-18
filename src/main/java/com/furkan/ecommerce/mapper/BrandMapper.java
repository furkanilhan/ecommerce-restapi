package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.BrandDTO;
import com.furkan.ecommerce.model.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    BrandDTO toBrandDTO(Brand brand);

    Brand toBrand(BrandDTO brandDTO);
}
