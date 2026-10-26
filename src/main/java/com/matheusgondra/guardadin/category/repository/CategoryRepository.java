package com.matheusgondra.guardadin.category.repository;

import com.matheusgondra.guardadin.category.entity.Category;
import com.matheusgondra.guardadin.category.entity.CategoryType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(CategoryType name);
}
