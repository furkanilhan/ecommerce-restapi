package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.*;
import com.furkan.ecommerce.enums.RoleName;
import com.furkan.ecommerce.model.*;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DTOToEntity {

    @Mapping(target = "roles", ignore = true)
    User toUser(UserDTO userDTO);

    @AfterMapping
    default void mapRoles(@MappingTarget User user, UserDTO userDTO) {
        Set<Role> roles = userDTO.getRoles().stream()
                .map(roleName -> new Role(RoleName.valueOf(roleName)))
                .collect(Collectors.toSet());
        user.setRoles(roles);
    }

    Product toProduct(ProductDetailDTO productDetailDTO);

    @AfterMapping
    default void linkProduct(@MappingTarget Product product) {
        if (product.getProductVariants() != null) {
            product.getProductVariants().forEach(variant -> variant.setProduct(product));
        }
    }

    Category toCategory(CategoryDTO categoryDTO);

    Color toColor(ColorDTO colorDTO);

    Variant toVariant(VariantDTO variantDTO);

    ProductType toProductType(ProductTypeDTO productTypeDTO);

    Brand toBrand(BrandDTO brandDTO);

    BrandModel toBrandModel(BrandModelDTO brandModelDTO);

    ProductVariant toProductVariant(ProductVariantDTO productVariantDTO);

    Cart toCart(CartDTO cartDTO);

    CartItem toCartItem(CartItemDTO cartItemDTO);
}
