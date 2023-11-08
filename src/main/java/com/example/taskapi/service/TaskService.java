package com.example.taskapi.service;

import com.example.taskapi.model.Task;
import com.example.taskapi.model.TaskStatus;
import com.example.taskapi.model.dto.AddTaskDto;
import com.example.taskapi.model.dto.TaskDto;
import com.example.taskapi.model.dto.UpdateTaskDto;
import com.example.taskapi.repository.TaskRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskService {
    private final TaskRepository taskRepository;

    public Page<TaskDto> getTasks(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(TaskDto::new);
    }

    public TaskDto getTask(Long id) {
        return taskRepository.findById(id)
                .map(TaskDto::new)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }

    public Long addTask(@NotNull @Valid AddTaskDto addTaskDto) {
        var task = Task.builder()
                .content(addTaskDto.getContent())
                .status(TaskStatus.TO_DO)
                .deadline(addTaskDto.getDeadline())
                .creationDate(LocalDate.now())
                .build();

        return taskRepository.save(task).getId();
    }

    public TaskDto updateTask(Long id, @NotNull @Valid UpdateTaskDto updateTaskDto) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task not found"));
        task.setContent(updateTaskDto.getContent());
        task.setStatus(updateTaskDto.getStatus());
        if (updateTaskDto.getDeadline() != null) {
            task.setDeadline(updateTaskDto.getDeadline());
        }

        return new TaskDto(taskRepository.save(task));
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
