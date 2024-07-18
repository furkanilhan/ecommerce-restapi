package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.CategoryDTO;
import com.furkan.ecommerce.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toCategoryDTO(Category category);

    Category toCategory(CategoryDTO categoryDTO);
}
