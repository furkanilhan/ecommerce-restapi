package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.CartDTO;
import com.furkan.ecommerce.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class, UserMapper.class})
public interface CartMapper {

    @Mapping(source = "cartItems", target = "cartItems")
    @Mapping(source = "user.id", target = "userId")
    CartDTO toCartDTO(Cart cart);

    @Mapping(source = "cartItems", target = "cartItems")
    @Mapping(source = "userId", target = "user.id")
    Cart toCart(CartDTO cartDTO);
}
