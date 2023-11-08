package com.example.taskapi;

import com.example.taskapi.model.Task;
import com.example.taskapi.model.TaskStatus;
import com.example.taskapi.model.dto.AddTaskDto;
import com.example.taskapi.model.dto.UpdateTaskDto;
import com.example.taskapi.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TaskRepository taskRepository;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private final Task task = Task.builder()
            .content("1")
            .status(TaskStatus.TO_DO)
            .creationDate(LocalDate.of(2020, 1, 2))
            .build();

    @AfterEach
    private void cleanUp() {
        taskRepository.deleteAll();
    }

    @Test
    public void shouldReturn404WhenTaskDoesntExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldGetAllTasks() throws Exception {
        var tasks = List.of(
                task,
                Task.builder()
                        .content("2")
                        .status(TaskStatus.TO_DO)
                        .creationDate(LocalDate.of(2021, 3, 14))
                        .build()
        );
        taskRepository.saveAll(tasks);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].content").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].creationDate").value("2020-01-02"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].content").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].creationDate").value("2021-03-14"));
    }

    @Test
    public void shouldGetTask() throws Exception {
        var id = taskRepository.save(task).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDate").value("2020-01-02"));
    }

    @Test
    public void shouldAddTask() throws Exception {
        var addTaskDto = new AddTaskDto("content", LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                        .content(objectMapper.writeValueAsString(addTaskDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void shouldUpdateTask() throws Exception {
        var id = taskRepository.save(task).getId();

        var updateTaskDto = new UpdateTaskDto("content", TaskStatus.IN_PROGRESS, LocalDate.of(2001, 10,2));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/" + id)
                        .content(objectMapper.writeValueAsString(updateTaskDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("content"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deadline").value("2001-10-02"));
    }

    @Test
    public void shouldDeleteTask() throws Exception {
        var id = taskRepository.save(task).getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldThrowBadRequest() throws Exception {
        var updateTaskDto = new UpdateTaskDto("content", TaskStatus.IN_PROGRESS, LocalDate.of(2001, 10,2));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/1")
                        .content(objectMapper.writeValueAsString(updateTaskDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
