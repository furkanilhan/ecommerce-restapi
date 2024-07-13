package com.furkan.ecommerce.service;

import com.furkan.ecommerce.dto.ProductDTO;
import com.furkan.ecommerce.dto.ProductDetailDTO;
import com.furkan.ecommerce.mapper.EntityToDTO;
import com.furkan.ecommerce.model.Category;
import com.furkan.ecommerce.model.Product;
import com.furkan.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private EntityToDTO entityToDTO;

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(entityToDTO::toProductDTO)
                .collect(Collectors.toList());
    }

    public ProductDetailDTO getProductById(Long id) {
        Product product =  productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        return entityToDTO.toProductDetailDTO(product);
    }

    public List<ProductDetailDTO> searchProducts(String query) {
        if (query.length() < 3) {
            throw new IllegalArgumentException("Search parameter must include at least 3 characters.");
        }
        String queryPart = "%" + query + "%";
        return productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(queryPart).stream()
                .map(entityToDTO::toProductDetailDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        List<Category> subCategories = categoryService.getAllSubCategories(categoryId);
        subCategories.add(categoryService.getCategoryById(categoryId));
        List<Long> categoryIds = subCategories.stream().map(Category::getId).collect(Collectors.toList());

        List<Product> products = productRepository.findByCategoryIdIn(categoryIds);
        return products.stream()
                .map(entityToDTO::toProductDTO)
                .collect(Collectors.toList());
    }
}
