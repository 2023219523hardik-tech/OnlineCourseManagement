package com.ocms.assignment.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssignmentDto {
    
    @NotBlank(message = "Assignment title is required")
    private String title;
    
    private String description;
    
    @NotNull(message = "Course ID is required")
    private Long courseId;
    
    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    private LocalDateTime dueDate;
    
    @NotNull(message = "Max score is required")
    @Positive(message = "Max score must be positive")
    private Integer maxScore;
}