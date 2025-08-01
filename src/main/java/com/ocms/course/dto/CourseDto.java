package com.ocms.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseDto {
    
    @NotBlank(message = "Course title is required")
    private String title;
    
    private String description;
    
    @NotNull(message = "Instructor ID is required")
    private Long instructorId;
}