package com.library.library_management.service;

import com.library.library_management.dto.CategoryRequestDto;
import com.library.library_management.dto.CategoryResponseDto;
import com.library.library_management.entity.Category;
import com.library.library_management.exception.CategoryHasBooksException;
import com.library.library_management.exception.CategoryNotFoundException;
import com.library.library_management.repository.BookRepository;
import com.library.library_management.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    public CategoryService(CategoryRepository categoryRepository, BookRepository bookRepository) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public CategoryResponseDto create(CategoryRequestDto request) {
        Category category = new Category(request.getName(), request.getDescription());
        return toDto(categoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto findById(Long id) {
        return toDto(findCategory(id));
    }

    @Transactional
    public CategoryResponseDto update(Long id, CategoryRequestDto request) {
        Category category = findCategory(id);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return toDto(categoryRepository.save(category));
    }

    @Transactional
    public void delete(Long id) {
        Category category = findCategory(id);
        if (bookRepository.existsByCategory_Id(id)) {
            throw new CategoryHasBooksException("A category linked to books cannot be deleted");
        }
        categoryRepository.delete(category);
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }

    private CategoryResponseDto toDto(Category category) {
        return new CategoryResponseDto(category.getId(), category.getName(), category.getDescription());
    }
}
