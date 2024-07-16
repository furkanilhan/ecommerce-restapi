package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.dto.ProductVariantDTO;
import com.furkan.ecommerce.mapper.EntityToDTO;
import com.furkan.ecommerce.model.*;
import com.furkan.ecommerce.repository.ProductVariantRepository;
import com.furkan.ecommerce.service.ProductVariantService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductVariantServiceImpl implements ProductVariantService {

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EntityToDTO entityToDTO;

    @Override
    public List<ProductVariantDTO> filterProductVariants(Integer minQuantity, Integer maxQuantity,
                                                         BigDecimal minPrice, BigDecimal maxPrice,
                                                         Long colorId, Long variantId,
                                                         Long productTypeId, Long brandId, Long brandModelId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductVariant> cq = cb.createQuery(ProductVariant.class);
        Root<ProductVariant> root = cq.from(ProductVariant.class);

        cq.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (minQuantity != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("quantity"), minQuantity));
        }
        if (maxQuantity != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("quantity"), maxQuantity));
        }
        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }
        if (colorId != null) {
            Join<ProductVariant, Color> colorJoin = root.join("color");
            predicates.add(cb.equal(colorJoin.get("id"), colorId));
        }
        if (variantId != null) {
            Join<ProductVariant, Variant> variantJoin = root.join("variant");
            predicates.add(cb.equal(variantJoin.get("id"), variantId));
        }
        if (productTypeId != null) {
            Join<ProductVariant, Product> productJoin = root.join("product");
            predicates.add(cb.equal(productJoin.get("productType").get("id"), productTypeId));
        }
        if (brandId != null) {
            Join<ProductVariant, Product> productJoin = root.join("product");
            predicates.add(cb.equal(productJoin.get("brand").get("id"), brandId));
        }
        if (brandModelId != null) {
            Join<ProductVariant, Product> productJoin = root.join("product");
            predicates.add(cb.equal(productJoin.get("brandModel").get("id"), brandModelId));
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        TypedQuery<ProductVariant> query = entityManager.createQuery(cq);
        List<ProductVariant> resultList = query.getResultList();

        return resultList.stream()
                .map(entityToDTO::toProductVariantDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deceraseProductVariantsQuantity(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            ProductVariant productVariant = orderItem.getProductVariant();
            int quantity = orderItem.getQuantity();

            if (productVariant.getQuantity() < quantity) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock for product variant: " + productVariant.getId());
            }

            productVariant.setQuantity(productVariant.getQuantity() - quantity);
            productVariant.setReservedQuantity(productVariant.getReservedQuantity() - quantity);

            productVariantRepository.save(productVariant);
        }
    }

    @Override
    @Transactional
    public void increaseProductVariantsQuantity(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            ProductVariant productVariant = orderItem.getProductVariant();
            int quantity = orderItem.getQuantity();

            productVariant.setQuantity(productVariant.getQuantity() + quantity);

            productVariantRepository.save(productVariant);
        }
    }
}

