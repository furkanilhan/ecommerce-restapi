package com.furkan.ecommerce.repository;

import com.furkan.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByIsDeletedFalse(Pageable pageable);

    Product findByIdAndIsDeletedFalse(Long id);

    @Query("SELECT p FROM Product p WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))) AND p.isDeleted = false")
    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(@Param("query") String query, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id IN :categoryIds AND p.isDeleted = false")
    Page<Product> findByCategoryIdInAndNotDeleted(@Param("categoryIds") List<Long> categoryIds, Pageable pageable);
}
