package com.furkan.ecommerce.service;

import com.furkan.ecommerce.dto.ProductDTO;
import com.furkan.ecommerce.dto.ProductDetailDTO;
import java.util.List;

public interface ProductService {

    List<ProductDTO> getAllProducts();

    ProductDetailDTO getProductById(Long id);

    List<ProductDetailDTO> searchProducts(String query);

    List<ProductDTO> getProductsByCategory(Long categoryId);

    ProductDetailDTO addProduct(ProductDetailDTO productDetailDTO);

}
