package com.jonah.portfolio.expenses;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class ExpenseRepository {

    private final Map<UUID, Expense> expenses = new ConcurrentHashMap<>();

    public Collection<Expense> findAll() {
        return expenses.values();
    }

    public Optional<Expense> findById(UUID id) {
        return Optional.ofNullable(expenses.get(id));
    }

    public Expense save(Expense expense) {
        expenses.put(expense.getId(), expense);
        return expense;
    }

    public void delete(UUID id) {
        expenses.remove(id);
    }
}
