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

    @Override
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        Page<Product> productsPage = productRepository.findByIsDeletedFalse(pageable);
        return productsPage.map(entityToDTO::toProductDTO);
    }

    @Override
    public ProductDetailDTO getProductById(Long id) {
        Product product =  productRepository.findByIdAndIsDeletedFalse(id);
        if (product == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Product not found with id: " + id);
        }
        return entityToDTO.toProductDetailDTO(product);
    }

    @Override
    public Page<ProductDetailDTO> searchProducts(String queryPart, Pageable pageable) {
        Page<Product> products = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(queryPart, pageable);
        return products.map(entityToDTO::toProductDetailDTO);
    }

    @Override
    public Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable) {
        List<Category> subCategories = categoryService.getAllSubCategories(categoryId);
        subCategories.add(categoryService.getCategoryById(categoryId));
        List<Long> categoryIds = subCategories.stream().map(Category::getId).collect(Collectors.toList());

        Page<Product> products = productRepository.findByCategoryIdInAndNotDeleted(categoryIds, pageable);
        return products.map(entityToDTO::toProductDTO);
    }

    @Override
    @Transactional
    public ProductDetailDTO addProduct(ProductDetailDTO productDetailDTO) {
        try {
            Product product = dtoToEntity.toProduct(productDetailDTO);
            product = productRepository.save(product);
            return entityToDTO.toProductDetailDTO(product);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Product creation failed unexpectedly.", e);
        }
    }

    @Override
    @Transactional
    public ProductDetailDTO updateProduct(Long productId, ProductDetailDTO updatedProductDTO) {
        Product product = productRepository.findByIdAndIsDeletedFalse(productId);

        if (product == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Product not found with id: " + productId);
        }
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
        try {
            Product updatedProduct = productRepository.save(product);
            return entityToDTO.toProductDetailDTO(updatedProduct);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Product update failed unexpectedly.", e);
        }
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findByIdAndIsDeletedFalse(productId);

        if (product == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, "Product not found with id: " + productId);
        }

        product.setDeleted(true);
        product.getProductVariants().forEach(productVariant -> productVariant.setDeleted(true));

        try {
            productVariantRepository.saveAll(product.getProductVariants());
            productRepository.save(product);
        } catch (Exception ex) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting product with id: " + productId);
        }
    }
}
