package vn.itstar.services;

import java.util.List;
import java.util.Optional;

import vn.itstar.entity.Category;

public interface ICategoryService {

    List<Category> findAll();
    List<Category> search(String keyword);
    Category save(Category category);
    
    Optional<Category> findById(Integer id);
    void deleteById(Integer id);
}
