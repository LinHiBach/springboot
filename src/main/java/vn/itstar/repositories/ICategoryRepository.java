package vn.itstar.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.itstar.entity.Category;

public interface ICategoryRepository
        extends JpaRepository<Category, Integer> {

    List<Category> findByCategorynameContainingIgnoreCase(String keyword);
}