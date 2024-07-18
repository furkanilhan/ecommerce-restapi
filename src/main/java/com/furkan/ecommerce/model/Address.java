package com.furkan.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Address extends BaseEntity {

    private String name;

    private String addressDetail;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

}
