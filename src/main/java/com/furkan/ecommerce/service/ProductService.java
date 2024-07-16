package com.furkan.ecommerce.service;

import com.furkan.ecommerce.dto.ProductDTO;
import com.furkan.ecommerce.dto.ProductDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<ProductDTO> getAllProducts(Pageable pageable);

    ProductDetailDTO getProductById(Long id);

    Page<ProductDetailDTO> searchProducts(String query, Pageable pageable);

    Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable);

    ProductDetailDTO addProduct(ProductDetailDTO productDetailDTO);

    ProductDetailDTO updateProduct(Long productId, ProductDetailDTO updatedProductDTO);

    void deleteProduct(Long productId);
}
