package com.ocms.assignment.controller;

import com.ocms.assignment.dto.AssignmentDto;
import com.ocms.assignment.dto.GradeSubmissionDto;
import com.ocms.assignment.dto.SubmissionDto;
import com.ocms.assignment.entity.Assignment;
import com.ocms.assignment.entity.Submission;
import com.ocms.assignment.service.AssignmentService;
import com.ocms.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {
    
    private final AssignmentService assignmentService;
    
    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Assignment>> createAssignment(@Valid @RequestBody AssignmentDto assignmentDto) {
        Assignment assignment = assignmentService.createAssignment(assignmentDto);
        return ResponseEntity.ok(ApiResponse.success("Assignment created successfully", assignment));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Assignment>> getAssignmentById(@PathVariable Long id) {
        Assignment assignment = assignmentService.getAssignmentById(id);
        return ResponseEntity.ok(ApiResponse.success(assignment));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Assignment>>> getAllAssignments() {
        List<Assignment> assignments = assignmentService.getAllAssignments();
        return ResponseEntity.ok(ApiResponse.success(assignments));
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<Assignment>>> getAssignmentsByCourse(@PathVariable Long courseId) {
        List<Assignment> assignments = assignmentService.getAssignmentsByCourse(courseId);
        return ResponseEntity.ok(ApiResponse.success(assignments));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Assignment>> updateAssignment(@PathVariable Long id, 
                                                                  @Valid @RequestBody AssignmentDto assignmentDto) {
        Assignment assignment = assignmentService.updateAssignment(id, assignmentDto);
        return ResponseEntity.ok(ApiResponse.success("Assignment updated successfully", assignment));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.ok(ApiResponse.success("Assignment deleted successfully", null));
    }
    
    @PostMapping("/submit")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Submission>> submitAssignment(@Valid @RequestBody SubmissionDto submissionDto) {
        Submission submission = assignmentService.submitAssignment(submissionDto);
        return ResponseEntity.ok(ApiResponse.success("Assignment submitted successfully", submission));
    }
    
    @GetMapping("/submissions/assignment/{assignmentId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Submission>>> getSubmissionsByAssignment(@PathVariable Long assignmentId) {
        List<Submission> submissions = assignmentService.getSubmissionsByAssignment(assignmentId);
        return ResponseEntity.ok(ApiResponse.success(submissions));
    }
    
    @GetMapping("/submissions/user/{userId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Submission>>> getSubmissionsByUser(@PathVariable Long userId) {
        List<Submission> submissions = assignmentService.getSubmissionsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(submissions));
    }
    
    @GetMapping("/submissions/{id}")
    public ResponseEntity<ApiResponse<Submission>> getSubmissionById(@PathVariable Long id) {
        Submission submission = assignmentService.getSubmissionById(id);
        return ResponseEntity.ok(ApiResponse.success(submission));
    }
    
    @PostMapping("/submissions/{id}/grade")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Submission>> gradeSubmission(@PathVariable Long id, 
                                                                 @Valid @RequestBody GradeSubmissionDto gradeDto) {
        Submission submission = assignmentService.gradeSubmission(id, gradeDto);
        return ResponseEntity.ok(ApiResponse.success("Submission graded successfully", submission));
    }
    
    @GetMapping("/submissions/{assignmentId}/ungraded")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Submission>>> getUngradedSubmissions(@PathVariable Long assignmentId) {
        List<Submission> submissions = assignmentService.getUngradedSubmissions(assignmentId);
        return ResponseEntity.ok(ApiResponse.success(submissions));
    }
    
    @GetMapping("/submissions/{assignmentId}/graded")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Submission>>> getGradedSubmissions(@PathVariable Long assignmentId) {
        List<Submission> submissions = assignmentService.getGradedSubmissions(assignmentId);
        return ResponseEntity.ok(ApiResponse.success(submissions));
    }
    
    @GetMapping("/submissions/{assignmentId}/average-score")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Double>> getAverageScore(@PathVariable Long assignmentId) {
        Double averageScore = assignmentService.getAverageScore(assignmentId);
        return ResponseEntity.ok(ApiResponse.success(averageScore));
    }
    
    @GetMapping("/overdue")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Assignment>>> getOverdueAssignments() {
        List<Assignment> assignments = assignmentService.getOverdueAssignments();
        return ResponseEntity.ok(ApiResponse.success(assignments));
    }
    
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Assignment>>> getAssignmentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Assignment> assignments = assignmentService.getAssignmentsByDateRange(start, end);
        return ResponseEntity.ok(ApiResponse.success(assignments));
    }
    
    @GetMapping("/submissions/next-to-process")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Submission>> getNextSubmissionToProcess() {
        Submission submission = assignmentService.getNextSubmissionToProcess();
        return ResponseEntity.ok(ApiResponse.success(submission));
    }
    
    @GetMapping("/{assignmentId}/statistics")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAssignmentStatistics(@PathVariable Long assignmentId) {
        Map<String, Object> stats = assignmentService.getAssignmentStatistics(assignmentId);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}