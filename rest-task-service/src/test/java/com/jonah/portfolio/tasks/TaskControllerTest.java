package com.jonah.portfolio.tasks;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAndRetrieveTask() throws Exception {
        TaskRequest request = new TaskRequest("Write docs", "Draft README", false);

        String responseBody = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Write docs")))
                .andReturn().getResponse().getContentAsString();

        TaskResponse created = objectMapper.readValue(responseBody, TaskResponse.class);

        mockMvc.perform(get("/api/tasks/{id}", created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(created.id().toString())));
    }

    @Test
    void shouldReturnNotFoundForMissingTask() throws Exception {
        mockMvc.perform(get("/api/tasks/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldValidateTitle() throws Exception {
        TaskRequest request = new TaskRequest("", "Description", false);
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Validation failed")));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        TaskRequest request = new TaskRequest("Write docs", "Draft README", false);
        String responseBody = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        TaskResponse created = objectMapper.readValue(responseBody, TaskResponse.class);

        TaskRequest update = new TaskRequest("Ship docs", "Publish README", true);
        mockMvc.perform(put("/api/tasks/{id}", created.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Ship docs")))
                .andExpect(jsonPath("$.completed", is(true)));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        TaskRequest request = new TaskRequest("Write docs", "Draft README", false);
        String responseBody = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        TaskResponse created = objectMapper.readValue(responseBody, TaskResponse.class);

        mockMvc.perform(delete("/api/tasks/{id}", created.id()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tasks/{id}", created.id()))
                .andExpect(status().isNotFound());
    }
}
