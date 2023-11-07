package com.example.taskapi.service;

import com.example.taskapi.model.Task;
import com.example.taskapi.model.dto.AddTaskDto;
import com.example.taskapi.model.dto.TaskDto;
import com.example.taskapi.repository.TaskRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskService {
    private final TaskRepository taskRepository;

    public List<TaskDto> getTasks() {
        return taskRepository.findAll().stream()
                .map(TaskDto::new)
                .collect(Collectors.toList());
    }

    public TaskDto getTask(Long id) {
        return taskRepository.findById(id)
                .map(TaskDto::new)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    public Long addTask(@NotNull AddTaskDto addTaskDto) {
        var task = Task.builder()
                .content(addTaskDto.getContent())
                .creationDate(LocalDate.now())
                .build();

        return taskRepository.save(task).getId();
    }

    public Long updateTask(Long id, @NotNull AddTaskDto addTaskDto) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task not found"));
        task.setContent(addTaskDto.getContent());

        return taskRepository.save(task).getId();
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
