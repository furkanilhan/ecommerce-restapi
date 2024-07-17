package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.*;
import com.furkan.ecommerce.model.*;
import org.mapstruct.*;

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

    ProductDTO toProductDTO(Product product);

    ProductDetailDTO toProductDetailDTO(Product product);

    CategoryDTO toCategoryDTO(Category category);

    ProductVariantDTO toProductVariantDTO(ProductVariant productVariant);

    ColorDTO toColorDTO(Color color);

    VariantDTO toVariantDTO(Variant variant);

    ProductTypeDTO toProductTypeDTO(ProductType productType);

    BrandDTO toBrandDTO(Brand brand);

    BrandModelDTO toBrandModelDTO(BrandModel brandModel);

    CartDTO toCartDTO(Cart cart);

    CartItemDTO toCartItemDTO(CartItem cartItem);
}