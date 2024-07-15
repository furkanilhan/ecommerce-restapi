package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.*;
import com.furkan.ecommerce.model.*;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface EntityToDTO {

    @Mapping(target = "roles", ignore = true)
    UserDTO toUserDTO(User user);

    @AfterMapping
    default void mapRoles(@MappingTarget UserDTO userDTO, User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
        userDTO.setRoles(roleNames);
    }

//    @Mappings({
//            @Mapping(target = "category", source = "product.category"),
//            @Mapping(target = "brand", source = "product.brand"),
//            @Mapping(target = "brandModel", source = "product.brandModel"),
//    })
    ProductDTO toProductDTO(Product product);

    @Mapping(target = "productVariants", source = "product.productVariants")
    ProductDetailDTO toProductDetailDTO(Product product);

    CategoryDTO toCategoryDTO(Category category);

    @Mappings({
            @Mapping(target = "productId", source = "product.id"),
            @Mapping(target = "color", source = "color"),
            @Mapping(target = "variant", source = "variant")
    })
    ProductVariantDTO toProductVariantDTO(ProductVariant productVariant);

    List<ProductVariantDTO> toProductVariantDTOs(List<ProductVariant> productVariants);

    ColorDTO toColorDTO(Color color);

    VariantDTO toVariantDTO(Variant variant);

    ProductTypeDTO toProductTypeDTO(ProductType productType);

    BrandDTO toBrandDTO(Brand brand);

    BrandModelDTO toBrandModelDTO(BrandModel brandModel);

    @Mapping(target = "userId", source = "user.id")
    CartDTO toCartDTO(Cart cart);

    List<CartItemDTO> toCartItemDTOs(List<CartItem> cartItems);

    @Mapping(target = "productVariantId", source = "productVariant.id")
    CartItemDTO toCartItemDTO(CartItem cartItem);
}