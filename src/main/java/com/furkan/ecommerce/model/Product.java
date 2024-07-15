package com.furkan.ecommerce.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
public class Product extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ProductType productType;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Brand brand;

    @OneToOne
    @JoinColumn(nullable = false)
    private BrandModel brandModel;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> productVariants = new ArrayList<>();

}
