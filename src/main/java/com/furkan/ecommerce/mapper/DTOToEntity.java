package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.*;
import com.furkan.ecommerce.enums.RoleName;
import com.furkan.ecommerce.model.*;
import org.mapstruct.*;

import java.util.List;
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

    @Mappings({
            @Mapping(target = "category", source = "category"),
            @Mapping(target = "productVariants", source = "productVariants")
    })
    Product toProduct(ProductDetailDTO productDetailDTO);

    Category toCategory(CategoryDTO categoryDTO);

    Color toColor(ColorDTO colorDTO);

    Variant toVariant(VariantDTO variantDTO);

    ProductType toProductType(ProductTypeDTO productTypeDTO);

    Brand toBrand(BrandDTO brandDTO);

    BrandModel toBrandModel(BrandModelDTO brandModelDTO);

    @Mappings({
            @Mapping(target = "product", ignore = true),
            @Mapping(target = "color", source = "color"),
            @Mapping(target = "variant", source = "variant")
    })
    ProductVariant toProductVariant(ProductVariantDTO productVariantDTO);

    List<ProductVariant> toProductVariants(List<ProductVariantDTO> productVariantDTOs);

    @AfterMapping
    default void linkProduct(@MappingTarget Product product) {
        if (product.getProductVariants() != null) {
            product.getProductVariants().forEach(variant -> variant.setProduct(product));
        }
    }

    @Mapping(target = "user", ignore = true)
    Cart toCart(CartDTO cartDTO);

    @Mapping(target = "cart", ignore = true)
    CartItem toCartItem(CartItemDTO cartItemDTO);
}
