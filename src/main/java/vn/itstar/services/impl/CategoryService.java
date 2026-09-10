package vn.itstar.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.itstar.entity.Category;
import vn.itstar.repositories.ICategoryRepository;
import vn.itstar.services.ICategoryService;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {

    private final ICategoryRepository categoryRepository;

    public CategoryService(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
    
    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }
    
    @Override
    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }
    @Override
    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }
    
    @Override
    public List<Category> search(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return categoryRepository.findAll();
        }

        return categoryRepository
                .findByCategorynameContainingIgnoreCase(keyword.trim());
    }
}