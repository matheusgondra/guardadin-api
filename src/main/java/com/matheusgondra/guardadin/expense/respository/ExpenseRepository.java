package com.matheusgondra.guardadin.expense.respository;

import com.matheusgondra.guardadin.expense.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    @EntityGraph(attributePaths = {"category"})
    Page<Expense> findAllByUserId(UUID userId, Pageable pageable);
}
