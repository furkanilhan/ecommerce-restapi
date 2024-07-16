package com.furkan.ecommerce.service;

import com.furkan.ecommerce.model.Category;

import java.util.List;

public interface CategoryService {

    Category getCategoryById(Long categoryId);

    List<Category> getCategoriesByParentId(Long parentId);

    List<Category> getAllSubCategories(Long categoryId);

}
