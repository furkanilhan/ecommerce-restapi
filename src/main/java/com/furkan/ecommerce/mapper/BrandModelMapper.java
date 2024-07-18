package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.BrandModelDTO;
import com.furkan.ecommerce.model.BrandModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BrandMapper.class})
public interface BrandModelMapper {

    @Mapping(source = "brand", target = "brand")
    BrandModelDTO toBrandModelDTO(BrandModel brandModel);

    @Mapping(source = "brand", target = "brand")
    BrandModel toBrandModel(BrandModelDTO brandModelDTO);
}
