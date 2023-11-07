package com.example.taskapi.model.dto;

import com.example.taskapi.model.Task;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDto {
    private Long id;
    private String content;
    private LocalDate creationDate;

    public TaskDto(Task task) {
        id = task.getId();
        content = task.getContent();
        creationDate = task.getCreationDate();
    }
}
