package com.furkan.ecommerce.controller;

import com.furkan.ecommerce.dto.ProductDTO;
import com.furkan.ecommerce.dto.ProductDetailDTO;
import com.furkan.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> getProductById(@PathVariable Long id) {
        ProductDetailDTO productDetailDTO = productService.getProductById(id);
        return ResponseEntity.ok(productDetailDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDetailDTO>> searchProducts(@RequestParam String query) {
        List<ProductDetailDTO> products = productService.searchProducts(query);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductDetailDTO> addProduct(@Valid @RequestBody ProductDetailDTO productDetailDTO) {
        ProductDetailDTO savedProduct = productService.addProduct(productDetailDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    //TODO: product update
    //TODO: product delete
}

