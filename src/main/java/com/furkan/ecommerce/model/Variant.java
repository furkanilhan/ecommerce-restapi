package com.furkan.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Variant extends BaseEntity {

    @Column(nullable = false)
    private String variant;

    @Column(nullable = false)
    private String value;
}
