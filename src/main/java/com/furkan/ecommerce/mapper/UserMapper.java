package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.UserDTO;
import com.furkan.ecommerce.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CartMapper.class, RoleMapper.class})
public interface UserMapper {

    @Mapping(source = "roles", target = "roles")
    @Mapping(source = "cart", target = "cart")
    User toUser(UserDTO userDTO);

    @Mapping(source = "roles", target = "roles")
    @Mapping(source = "cart", target = "cart")
    UserDTO toUserDTO(User user);
}
