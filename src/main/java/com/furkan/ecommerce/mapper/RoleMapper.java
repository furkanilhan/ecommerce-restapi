package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.RoleDTO;
import com.furkan.ecommerce.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO toRoleDTO(Role role);
    Role toRole(RoleDTO roleDTO);
}
