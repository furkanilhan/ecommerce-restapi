package com.furkan.ecommerce.serviceImpl;

import com.furkan.ecommerce.exception.CustomException;
import com.furkan.ecommerce.model.Category;
import com.furkan.ecommerce.repository.CategoryRepository;
import com.furkan.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Category not found with id: " + categoryId));
    }

    public List<Category> getCategoriesByParentId(Long parentId) {
        return categoryRepository.findByParentCategoryId(parentId);
    }

    public List<Category> getAllSubCategories(Long categoryId) {
        return getCategoriesByParentId(categoryId);
    }
}
