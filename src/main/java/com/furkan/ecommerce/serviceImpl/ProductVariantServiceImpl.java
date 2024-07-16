package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.dto.ProductVariantDTO;
import com.furkan.ecommerce.dto.ProductVariantFilterDTO;
import com.furkan.ecommerce.exception.CustomException;
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
    public List<ProductVariantDTO> filterProductVariants(ProductVariantFilterDTO filterDTO) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<ProductVariant> cq = cb.createQuery(ProductVariant.class);
            Root<ProductVariant> root = cq.from(ProductVariant.class);

            cq.select(root);

            List<Predicate> predicates = new ArrayList<>();
            if (filterDTO.getMinQuantity() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("quantity"), filterDTO.getMinQuantity()));
            }
            if (filterDTO.getMaxQuantity() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("quantity"), filterDTO.getMaxQuantity()));
            }
            if (filterDTO.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), filterDTO.getMinPrice()));
            }
            if (filterDTO.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), filterDTO.getMaxPrice()));
            }
            if (filterDTO.getColorIds() != null && !filterDTO.getColorIds().isEmpty()) {
                Join<ProductVariant, Color> colorJoin = root.join("color");
                predicates.add(colorJoin.get("id").in(filterDTO.getColorIds()));
            }
            if (filterDTO.getVariantIds() != null && !filterDTO.getVariantIds().isEmpty()) {
                Join<ProductVariant, Variant> variantJoin = root.join("variant");
                predicates.add(variantJoin.get("id").in(filterDTO.getVariantIds()));
            }
            if (filterDTO.getProductTypeIds() != null && !filterDTO.getProductTypeIds().isEmpty()) {
                Join<ProductVariant, Product> productJoin = root.join("product");
                predicates.add(productJoin.get("productType").get("id").in(filterDTO.getProductTypeIds()));
            }
            if (filterDTO.getBrandIds() != null && !filterDTO.getBrandIds().isEmpty()) {
                Join<ProductVariant, Product> productJoin = root.join("product");
                predicates.add(productJoin.get("brand").get("id").in(filterDTO.getBrandIds()));
            }
            if (filterDTO.getBrandModelIds() != null && !filterDTO.getBrandModelIds().isEmpty()) {
                Join<ProductVariant, Product> productJoin = root.join("product");
                predicates.add(productJoin.get("brandModel").get("id").in(filterDTO.getBrandModelIds()));
            }

            if (!predicates.isEmpty()) {
                cq.where(predicates.toArray(new Predicate[0]));
            }

            TypedQuery<ProductVariant> query = entityManager.createQuery(cq);
            List<ProductVariant> resultList = query.getResultList();

            return resultList.stream()
                    .map(entityToDTO::toProductVariantDTO)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while filtering product variants. Please try again later.", ex);
        }
    }

    @Override
    @Transactional
    public void decreaseProductVariantsQuantity(List<OrderItem> orderItems) {
        try {
            for (OrderItem orderItem : orderItems) {
                ProductVariant productVariant = orderItem.getProductVariant();
                int quantity = orderItem.getQuantity();

                if (productVariant.getQuantity() < quantity) {
                    throw new CustomException(HttpStatus.BAD_REQUEST, "Insufficient stock for product variant: " + productVariant.getId());
                }

                productVariant.setQuantity(productVariant.getQuantity() - quantity);
                productVariant.setReservedQuantity(productVariant.getReservedQuantity() - quantity);

                productVariantRepository.save(productVariant);
            }
        } catch (RuntimeException ex) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to decrease product variants quantity.", ex);
        }
    }

    @Override
    @Transactional
    public void increaseProductVariantsQuantity(List<OrderItem> orderItems) {
        try {
            for (OrderItem orderItem : orderItems) {
                ProductVariant productVariant = orderItem.getProductVariant();
                int quantity = orderItem.getQuantity();

                productVariant.setQuantity(productVariant.getQuantity() + quantity);

                productVariantRepository.save(productVariant);
            }
        } catch (RuntimeException ex) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to increase product variants quantity.", ex);
        }
    }
}
