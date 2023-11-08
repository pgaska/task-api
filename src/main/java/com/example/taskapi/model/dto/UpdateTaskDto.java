package com.example.taskapi.model.dto;

import com.example.taskapi.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskDto {
    @NotBlank
    private String content;

    @NotNull
    private TaskStatus status;

    private LocalDate deadline;
}
