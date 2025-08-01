package com.ocms.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ModuleDto {
    
    @NotBlank(message = "Module title is required")
    private String moduleTitle;
    
    private String content;
    
    @NotNull(message = "Module order is required")
    private Integer moduleOrder;
    
    @NotNull(message = "Course ID is required")
    private Long courseId;
}