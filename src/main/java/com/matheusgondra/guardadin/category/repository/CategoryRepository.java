package com.matheusgondra.guardadin.category.repository;

import com.matheusgondra.guardadin.category.entity.Category;
import com.matheusgondra.guardadin.category.entity.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(CategoryType name);
}
