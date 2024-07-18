package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.ProductDTO;
import com.furkan.ecommerce.dto.ProductDetailDTO;
import com.furkan.ecommerce.model.Product;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {BrandMapper.class, BrandModelMapper.class,
        CategoryMapper.class, ProductTypeMapper.class, ProductVariantMapper.class})
public interface ProductMapper {

    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "brandModel", target = "brandModel")
    @Mapping(source = "category", target = "category")
    ProductDTO toProductDTO(Product product);

    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "brandModel", target = "brandModel")
    @Mapping(source = "category", target = "category")
    Product toProduct(ProductDetailDTO productDetailDTO);

    @AfterMapping
    default void linkProduct(@MappingTarget Product product) {
        if (product.getProductVariants() != null) {
            product.getProductVariants().forEach(variant -> variant.setProduct(product));
        }
    }

    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "brandModel", target = "brandModel")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "productType", target = "productType")
    @Mapping(source = "productVariants", target = "productVariants")
    ProductDetailDTO toProductDetailDTO(Product product);

}
