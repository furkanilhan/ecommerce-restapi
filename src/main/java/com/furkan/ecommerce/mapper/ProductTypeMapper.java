package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.ProductTypeDTO;
import com.furkan.ecommerce.model.ProductType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductTypeMapper {

    ProductTypeDTO toProductTypeDTO(ProductType productType);

    ProductType toProductType(ProductTypeDTO productTypeDTO);
}
