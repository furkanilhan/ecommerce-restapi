package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.AttributeDTO;
import com.furkan.ecommerce.dto.AttributeTypeDTO;
import com.furkan.ecommerce.dto.CategoryDTO;
import com.furkan.ecommerce.dto.ProductDTO;
import com.furkan.ecommerce.model.Attribute;
import com.furkan.ecommerce.model.AttributeType;
import com.furkan.ecommerce.model.Category;
import com.furkan.ecommerce.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityToDTO {
    public ProductDTO toProductDTO(Product product) {
        if (product == null) {
            return null;
        }
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        dto.setCategory(toCategoryDTO(product.getCategory()));
        dto.setAttributes(toAttributeDTOs(product.getAttributes()));
        return dto;
    }

    public CategoryDTO toCategoryDTO(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    public List<AttributeDTO> toAttributeDTOs(List<Attribute> attributes) {
        if (attributes == null) {
            return null;
        }
        return attributes.stream()
                .map(attribute -> {
                    AttributeDTO dto = new AttributeDTO();
                    dto.setId(attribute.getId());
                    dto.setValue(attribute.getValue());
                    dto.setAttributeType(toAttributeTypeDTO(attribute.getAttributeType()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public AttributeTypeDTO toAttributeTypeDTO(AttributeType attributeType) {
        if (attributeType == null) {
            return null;
        }
        AttributeTypeDTO dto = new AttributeTypeDTO();
        dto.setId(attributeType.getId());
        dto.setName(attributeType.getName());
        return dto;
    }
}
