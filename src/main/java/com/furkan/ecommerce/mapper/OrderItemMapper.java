package com.furkan.ecommerce.mapper;

import com.furkan.ecommerce.dto.OrderItemDTO;
import com.furkan.ecommerce.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductVariantMapper.class})
public interface OrderItemMapper {

    @Mapping(source = "customerOrder.id", target = "customerOrderId")
    @Mapping(source = "productVariant", target = "productVariant")
    OrderItemDTO toOrderItemDTO(OrderItem orderItem);

    @Mapping(source = "customerOrderId", target = "customerOrder.id")
    @Mapping(source = "productVariant", target = "productVariant")
    OrderItem toOrderItem(OrderItemDTO orderItemDTO);
}
