package com.ocms.reporting.controller;

import com.ocms.common.dto.ApiResponse;
import com.ocms.reporting.dto.ReportDto;
import com.ocms.reporting.service.ReportingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportingController {
    
    private final ReportingService reportingService;
    
    @PostMapping("/custom")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateCustomReport(@Valid @RequestBody ReportDto reportDto) {
        Map<String, Object> report = reportingService.generateCustomReport(reportDto);
        return ResponseEntity.ok(ApiResponse.success("Report generated successfully", report));
    }
    
    @GetMapping("/student-performance/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateStudentPerformanceReport(
            @PathVariable Long studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> report = reportingService.generateStudentPerformanceReport(studentId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Student performance report generated successfully", report));
    }
    
    @GetMapping("/course-completion/{courseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateCourseCompletionReport(@PathVariable Long courseId) {
        Map<String, Object> report = reportingService.generateCourseCompletionReport(courseId);
        return ResponseEntity.ok(ApiResponse.success("Course completion report generated successfully", report));
    }
    
    @GetMapping("/assignment-statistics/{assignmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateAssignmentStatisticsReport(@PathVariable Long assignmentId) {
        Map<String, Object> report = reportingService.generateAssignmentStatisticsReport(assignmentId);
        return ResponseEntity.ok(ApiResponse.success("Assignment statistics report generated successfully", report));
    }
    
    @GetMapping("/system-activity")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateSystemActivityReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> report = reportingService.generateSystemActivityReport(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("System activity report generated successfully", report));
    }
    
    @GetMapping("/user-activity/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateUserActivityReport(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> report = reportingService.generateUserActivityReport(userId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("User activity report generated successfully", report));
    }
    
    @GetMapping("/instructor-performance/{instructorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateInstructorPerformanceReport(
            @PathVariable Long instructorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> report = reportingService.generateInstructorPerformanceReport(instructorId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Instructor performance report generated successfully", report));
    }
    
    @GetMapping("/activity-log")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TreeMap<LocalDateTime, Object>>> getActivityLog() {
        TreeMap<LocalDateTime, Object> activityLog = reportingService.getActivityLog();
        return ResponseEntity.ok(ApiResponse.success(activityLog));
    }
    
    @PostMapping("/log-activity")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> logActivity(@RequestParam String activity) {
        reportingService.logActivity(activity);
        return ResponseEntity.ok(ApiResponse.success("Activity logged successfully", null));
    }
}