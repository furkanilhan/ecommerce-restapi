package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.UserDTO;
import com.furkan.ecommerce.dto.UserDetailDTO;
import com.furkan.ecommerce.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CartMapper.class, RoleMapper.class, AddressMapper.class})
public interface UserMapper {

    @Mapping(source = "roles", target = "roles")
    User toUser(UserDTO userDTO);

    @Mapping(source = "roles", target = "roles")
    @Mapping(source = "cart", target = "cart")
    @Mapping(source = "addresses", target = "addresses")
    User toUser(UserDetailDTO userDetailDTO);

    @Mapping(source = "roles", target = "roles")
    UserDTO toUserDTO(User user);

    @Mapping(source = "roles", target = "roles")
    @Mapping(source = "cart", target = "cart")
    @Mapping(source = "addresses", target = "addresses")
    UserDetailDTO toUserDetailDTO(User user);

}
