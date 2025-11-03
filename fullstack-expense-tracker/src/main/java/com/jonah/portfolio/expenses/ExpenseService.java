package com.jonah.portfolio.expenses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    public List<Expense> list() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Expense::getDate).reversed())
                .toList();
    }

    public Expense create(ExpenseRequest request) {
        Expense expense = new Expense(UUID.randomUUID(), request.category(), request.amount(), request.date(), request.note());
        repository.save(expense);
        return expense;
    }

    public Expense update(UUID id, ExpenseRequest request) {
        Expense expense = repository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));
        expense.setCategory(request.category());
        expense.setAmount(request.amount());
        expense.setDate(request.date());
        expense.setNote(request.note());
        repository.save(expense);
        return expense;
    }

    public void delete(UUID id) {
        if (repository.findById(id).isEmpty()) {
            throw new ExpenseNotFoundException(id);
        }
        repository.delete(id);
    }

    public BigDecimal totalForMonth(LocalDate month) {
        return list().stream()
                .filter(expense -> expense.getDate().getYear() == month.getYear()
                        && expense.getDate().getMonth() == month.getMonth())
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<String, BigDecimal> totalsByCategory() {
        return list().stream()
                .collect(Collectors.groupingBy(Expense::getCategory,
                        Collectors.mapping(Expense::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
    }
}
