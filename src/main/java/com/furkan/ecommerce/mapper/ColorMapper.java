package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.ColorDTO;
import com.furkan.ecommerce.model.Color;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ColorMapper {

    ColorDTO toColorDTO(Color color);

    Color toColor(ColorDTO colorDTO);
}
