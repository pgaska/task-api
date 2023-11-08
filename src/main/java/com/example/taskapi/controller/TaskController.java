package com.example.taskapi.controller;

import com.example.taskapi.model.dto.AddTaskDto;
import com.example.taskapi.model.dto.TaskDto;
import com.example.taskapi.model.dto.UpdateTaskDto;
import com.example.taskapi.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public Page<TaskDto> getTasks(Pageable pageable) {
        return taskService.getTasks(pageable);
    }

    @GetMapping("/{id}")
    public TaskDto getTask(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long addTask(
            @RequestBody @NotNull @Valid AddTaskDto addTaskDto
    ) {
        return taskService.addTask(addTaskDto);
    }

    @PutMapping("/{id}")
    public TaskDto updateTask(
            @PathVariable Long id,
            @RequestBody @NotNull @Valid UpdateTaskDto updateTaskDto
    ) {
        return taskService.updateTask(id, updateTaskDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
