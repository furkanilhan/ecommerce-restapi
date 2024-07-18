package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.AddressDTO;
import com.furkan.ecommerce.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(source = "user.id", target = "userId")
    AddressDTO toAddressDTO(Address address);

    @Mapping(source = "userId", target = "user.id")
    Address toAddress(AddressDTO addressDTO);

}
