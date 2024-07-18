package com.furkan.ecommerce.dto;

import com.furkan.ecommerce.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOrderDTO {
    private Long id;
    private Long userId;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private List<OrderItemDTO> items;
}
