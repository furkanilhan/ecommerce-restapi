package com.furkan.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class ProductVariant extends BaseEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Color color;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Variant variant;

    @Column(nullable = false)
    private Integer quantity;

    private Integer reservedQuantity;

    @Column(nullable = false)
    private BigDecimal price;

}
