package com.jonah.portfolio.expenses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseResponse(UUID id, String category, BigDecimal amount, LocalDate date, String note) {
    public static ExpenseResponse from(Expense expense) {
        return new ExpenseResponse(expense.getId(), expense.getCategory(), expense.getAmount(), expense.getDate(), expense.getNote());
    }
}
