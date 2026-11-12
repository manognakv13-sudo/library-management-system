package com.library.library_management.service;

import com.library.library_management.dto.CategoryRequestDto;
import com.library.library_management.dto.CategoryResponseDto;
import com.library.library_management.entity.Category;
import com.library.library_management.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDto create(CategoryRequestDto request) {
        Category saved = categoryRepository.save(
                new Category(request.getName(), request.getDescription()));
        return toDto(saved);
    }

    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findAll().stream().map(this::toDto).toList();
    }

    private CategoryResponseDto toDto(Category category) {
        return new CategoryResponseDto(category.getId(), category.getName(), category.getDescription());
    }
}
