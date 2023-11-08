package com.example.taskapi.model.dto;

import com.example.taskapi.model.Task;
import com.example.taskapi.model.TaskStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDto {
    private Long id;
    private String content;
    private TaskStatus status;
    private LocalDate deadline;
    private LocalDate creationDate;

    public TaskDto(Task task) {
        id = task.getId();
        content = task.getContent();
        status = task.getStatus();
        deadline = task.getDeadline();
        creationDate = task.getCreationDate();
    }
}
