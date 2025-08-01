package com.ocms.reporting.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDto {
    
    @NotNull(message = "Report type is required")
    private String reportType;
    
    private LocalDateTime startDate;
    
    private LocalDateTime endDate;
    
    private Long courseId;
    
    private Long userId;
}