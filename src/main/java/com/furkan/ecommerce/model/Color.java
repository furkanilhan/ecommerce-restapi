package com.furkan.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Color extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;
}
