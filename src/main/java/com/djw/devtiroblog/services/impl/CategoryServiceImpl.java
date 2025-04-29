package com.djw.devtiroblog.services.impl;

import com.djw.devtiroblog.domain.entities.Category;
import com.djw.devtiroblog.repositories.CategoryRepository;
import com.djw.devtiroblog.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
//@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        if(categoryRepository.existsByNameIgnoreCase(category.getName())){
            throw new IllegalArgumentException("Category already exists with name: " + category.getName());
        }

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        Category category = getCategoryById(id);

        if(!category.getPosts().isEmpty())
            throw new IllegalStateException("Cannot Delete Category : " + category.getName() + " it has associated posts");

        categoryRepository.delete(category);
    }

    private Category getCategoryById(UUID id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }
}
