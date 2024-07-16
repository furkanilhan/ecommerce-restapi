package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.dto.ProductDTO;
import com.furkan.ecommerce.dto.ProductDetailDTO;
import com.furkan.ecommerce.exception.CustomException;
import com.furkan.ecommerce.mapper.DTOToEntity;
import com.furkan.ecommerce.mapper.EntityToDTO;
import com.furkan.ecommerce.model.*;
import com.furkan.ecommerce.repository.ProductRepository;
import com.furkan.ecommerce.repository.ProductVariantRepository;
import com.furkan.ecommerce.service.CategoryService;
import com.furkan.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private EntityToDTO entityToDTO;

    @Autowired
    private DTOToEntity dtoToEntity;

    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        Page<Product> productsPage = productRepository.findByIsDeletedFalse(pageable);
        return productsPage.map(entityToDTO::toProductDTO);
    }

    public ProductDetailDTO getProductById(Long id) {
        Product product =  productRepository.findByIdAndIsDeletedFalse(id);
        if (product == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Product not found with id: " + id);
        }
        return entityToDTO.toProductDetailDTO(product);
    }

    public Page<ProductDetailDTO> searchProducts(String queryPart, Pageable pageable) {
        Page<Product> products = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(queryPart, pageable);
        return products.map(entityToDTO::toProductDetailDTO);
    }

    public Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable) {
        List<Category> subCategories = categoryService.getAllSubCategories(categoryId);
        subCategories.add(categoryService.getCategoryById(categoryId));
        List<Long> categoryIds = subCategories.stream().map(Category::getId).collect(Collectors.toList());

        Page<Product> products = productRepository.findByCategoryIdInAndNotDeleted(categoryIds, pageable);
        return products.map(entityToDTO::toProductDTO);
    }

    @Transactional
    public ProductDetailDTO addProduct(ProductDetailDTO productDetailDTO) {
        try {
            Product product = dtoToEntity.toProduct(productDetailDTO);
            product = productRepository.save(product);
            return entityToDTO.toProductDetailDTO(product);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Product creation failed: " + e.getRootCause().getMessage(), e);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Product creation failed unexpectedly.", e);
        }
    }

    @Transactional
    public ProductDetailDTO updateProduct(Long productId, ProductDetailDTO updatedProductDTO) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Product not found with id: " + productId));

            product.setName(updatedProductDTO.getName());
            product.setDescription(updatedProductDTO.getDescription());

            Category category = new Category();
            category.setId(updatedProductDTO.getCategory().getId());
            product.setCategory(category);

            ProductType productType = new ProductType();
            productType.setId(updatedProductDTO.getProductType().getId());
            product.setProductType(productType);

            Brand brand = new Brand();
            brand.setId(updatedProductDTO.getBrand().getId());
            product.setBrand(brand);

            BrandModel brandModel = new BrandModel();
            brandModel.setId(updatedProductDTO.getBrandModel().getId());
            product.setBrandModel(brandModel);

            Product updatedProduct = productRepository.save(product);
            return entityToDTO.toProductDetailDTO(updatedProduct);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Product update failed: " + e.getRootCause().getMessage(), e);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Product update failed unexpectedly.", e);
        }
    }

    @Transactional
    public void deleteProduct(Long productId) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Product not found with id: " + productId));

            product.setDeleted(true);
            product.getProductVariants().forEach(productVariant -> productVariant.setDeleted(true));

            productVariantRepository.saveAll(product.getProductVariants());
            productRepository.save(product);
        } catch (DataAccessException ex) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting product with id: " + productId);
        } catch (Exception ex) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error deleting product with id: " + productId);
        }
    }
}
