package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.CartItemDTO;
import com.furkan.ecommerce.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductVariantMapper.class})
public interface CartItemMapper {

    @Mapping(source = "cart.id", target = "cartId")
    @Mapping(source = "productVariant", target = "productVariant")
    CartItemDTO toCartItemDTO(CartItem cartItem);

    @Mapping(source = "cartId", target = "cart.id")
    @Mapping(source = "productVariant", target = "productVariant")
    CartItem toCartItem(CartItemDTO cartItemDTO);
}
