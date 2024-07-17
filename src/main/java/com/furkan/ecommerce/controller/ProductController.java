package com.furkan.ecommerce.controller;

import com.furkan.ecommerce.dto.ProductDTO;
import com.furkan.ecommerce.dto.ProductDetailDTO;
import com.furkan.ecommerce.exception.CustomException;
import com.furkan.ecommerce.payload.response.MessageResponse;
import com.furkan.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiPrefix}/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> getProductById(@PathVariable Long id) {
        ProductDetailDTO productDetailDTO = productService.getProductById(id);
        return ResponseEntity.ok(productDetailDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDetailDTO>> searchProducts(@RequestParam String query, Pageable pageable) {
        if (query.length() < 3) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Search parameter must include at least 3 characters.");
        }
        String queryPart = "%" + query + "%";
        Page<ProductDetailDTO> products = productService.searchProducts(queryPart, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId, Pageable pageable) {
        Page<ProductDTO> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductDetailDTO> addProduct(@Valid @RequestBody ProductDetailDTO productDetailDTO) {
        ProductDetailDTO savedProduct = productService.addProduct(productDetailDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductDetailDTO> updateProduct(@Valid @PathVariable Long id, @RequestBody ProductDetailDTO productDetailDTO) {
        ProductDetailDTO savedProduct = productService.updateProduct(id, productDetailDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MessageResponse> deleteProduct(@Valid @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new MessageResponse("Product deleted successfully"));
    }
}

