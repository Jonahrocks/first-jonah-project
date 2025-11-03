package com.jonah.portfolio.expenses.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonah.portfolio.expenses.Expense;
import com.jonah.portfolio.expenses.ExpenseRequest;
import com.jonah.portfolio.expenses.ExpenseService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ExpenseController.class)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExpenseService service;

    private Expense expense;

    @BeforeEach
    void setUp() {
        expense = new Expense(UUID.randomUUID(), "Food", new BigDecimal("12.50"), LocalDate.of(2024, 5, 15), "Lunch");
    }

    @Test
    void shouldListExpenses() throws Exception {
        when(service.list()).thenReturn(List.of(expense));

        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].category", is("Food")));
    }

    @Test
    void shouldCreateExpense() throws Exception {
        when(service.create(any())).thenReturn(expense);
        ExpenseRequest request = new ExpenseRequest("Food", new BigDecimal("12.50"), LocalDate.of(2024, 5, 15), "Lunch");

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.category", is("Food")));
    }

    @Test
    void shouldUpdateExpense() throws Exception {
        when(service.update(any(), any())).thenReturn(expense);
        ExpenseRequest request = new ExpenseRequest("Food", new BigDecimal("12.50"), LocalDate.of(2024, 5, 15), "Lunch");

        mockMvc.perform(put("/api/expenses/{id}", expense.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category", is("Food")));
    }

    @Test
    void shouldDeleteExpense() throws Exception {
        mockMvc.perform(delete("/api/expenses/{id}", expense.getId()))
                .andExpect(status().isNoContent());
        verify(service).delete(expense.getId());
    }

    @Test
    void shouldReturnTotals() throws Exception {
        when(service.totalForMonth(any())).thenReturn(new BigDecimal("42.00"));
        when(service.totalsByCategory()).thenReturn(Map.of("Food", new BigDecimal("42.00")));

        mockMvc.perform(get("/api/expenses/summary/monthly").param("date", "2024-05-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(42.00)));

        mockMvc.perform(get("/api/expenses/summary/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Food", is(42.00)));
    }
}
