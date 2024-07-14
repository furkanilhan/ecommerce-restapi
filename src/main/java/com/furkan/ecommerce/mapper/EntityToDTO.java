package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.ProductDetailDTO;
import com.furkan.ecommerce.dto.ProductVariantDTO;
import com.furkan.ecommerce.dto.CategoryDTO;
import com.furkan.ecommerce.dto.ProductDTO;
import com.furkan.ecommerce.model.ProductVariant;
import com.furkan.ecommerce.model.Category;
import com.furkan.ecommerce.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EntityToDTO {

    @Mapping(target = "category", source = "product.category")
    ProductDTO toProductDTO(Product product);

    @Mapping(target = "productVariants", source = "product.productVariants")
    ProductDetailDTO toProductDetailDTO(Product product);

    CategoryDTO toCategoryDTO(Category category);

    @Mapping(target = "productId", source = "product.id")
    ProductVariantDTO toProductVariantDTO(ProductVariant productVariant);

    List<ProductVariantDTO> toProductVariantDTOs(List<ProductVariant> productVariants);
}