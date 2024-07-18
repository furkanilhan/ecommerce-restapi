package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.CustomerOrderDTO;
import com.furkan.ecommerce.model.CustomerOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, OrderItemMapper.class})
public interface CustomerOrderMapper {

    @Mapping(source = "items", target = "items")
    @Mapping(source = "user.id", target = "userId")
    CustomerOrderDTO toCustomerOrderDTO(CustomerOrder customerOrder);

    @Mapping(source = "items", target = "items")
    @Mapping(source = "userId", target = "user.id")
    CustomerOrder toCustomerOrder(CustomerOrderDTO customerOrderDTO);
}
