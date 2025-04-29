package com.djw.devtiroblog.services;

import com.djw.devtiroblog.domain.entities.Category;

import java.util.List;

public interface CategoryService {
    List<Category> listCategories();
    Category createCategory(Category category);
}
