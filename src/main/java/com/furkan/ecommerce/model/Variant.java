package com.furkan.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Variant extends BaseEntity {

    @Column(nullable = false)
    private String variant;

    @Column(nullable = false)
    private String value;
}
