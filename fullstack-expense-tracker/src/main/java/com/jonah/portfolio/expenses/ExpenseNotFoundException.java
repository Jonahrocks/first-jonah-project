package com.jonah.portfolio.expenses;

import java.util.UUID;

public class ExpenseNotFoundException extends RuntimeException {
    public ExpenseNotFoundException(UUID id) {
        super("Expense %s not found".formatted(id));
    }
}
