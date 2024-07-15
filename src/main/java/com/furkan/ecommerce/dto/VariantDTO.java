package com.furkan.ecommerce.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VariantDTO {
    private Long id;
    private String variant;
    private String value;
}
