package com.ocms.assignment.service;

import com.ocms.assignment.dto.AssignmentDto;
import com.ocms.assignment.dto.GradeSubmissionDto;
import com.ocms.assignment.dto.SubmissionDto;
import com.ocms.assignment.entity.Assignment;
import com.ocms.assignment.entity.Submission;
import com.ocms.assignment.repository.AssignmentRepository;
import com.ocms.assignment.repository.SubmissionRepository;
import com.ocms.common.datastructures.PriorityQueue;
import com.ocms.common.exception.ResourceNotFoundException;
import com.ocms.course.entity.Course;
import com.ocms.course.service.CourseService;
import com.ocms.user.entity.User;
import com.ocms.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssignmentService {
    
    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;
    private final CourseService courseService;
    private final UserService userService;
    
    // PriorityQueue for handling assignment submissions based on deadlines
    private final PriorityQueue<Submission> submissionQueue = new PriorityQueue<>(
        Comparator.comparing(s -> s.getAssignment().getDueDate())
    );
    
    public Assignment createAssignment(AssignmentDto assignmentDto) {
        Course course = courseService.getCourseById(assignmentDto.getCourseId());
        
        Assignment assignment = new Assignment();
        assignment.setTitle(assignmentDto.getTitle());
        assignment.setDescription(assignmentDto.getDescription());
        assignment.setCourse(course);
        assignment.setDueDate(assignmentDto.getDueDate());
        assignment.setMaxScore(assignmentDto.getMaxScore());
        
        return assignmentRepository.save(assignment);
    }
    
    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));
    }
    
    public List<Assignment> getAssignmentsByCourse(Long courseId) {
        return assignmentRepository.findByCourseIdAndIsActiveTrue(courseId);
    }
    
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }
    
    public Assignment updateAssignment(Long id, AssignmentDto assignmentDto) {
        Assignment assignment = getAssignmentById(id);
        
        if (assignmentDto.getTitle() != null) {
            assignment.setTitle(assignmentDto.getTitle());
        }
        if (assignmentDto.getDescription() != null) {
            assignment.setDescription(assignmentDto.getDescription());
        }
        if (assignmentDto.getDueDate() != null) {
            assignment.setDueDate(assignmentDto.getDueDate());
        }
        if (assignmentDto.getMaxScore() != null) {
            assignment.setMaxScore(assignmentDto.getMaxScore());
        }
        if (assignmentDto.getCourseId() != null) {
            Course course = courseService.getCourseById(assignmentDto.getCourseId());
            assignment.setCourse(course);
        }
        
        return assignmentRepository.save(assignment);
    }
    
    public void deleteAssignment(Long id) {
        Assignment assignment = getAssignmentById(id);
        assignment.setActive(false);
        assignmentRepository.save(assignment);
    }
    
    public Submission submitAssignment(SubmissionDto submissionDto) {
        Assignment assignment = getAssignmentById(submissionDto.getAssignmentId());
        User user = userService.getUserById(submissionDto.getUserId());
        
        // Check if already submitted
        Optional<Submission> existingSubmission = submissionRepository
                .findByAssignmentIdAndUserId(submissionDto.getAssignmentId(), submissionDto.getUserId());
        
        if (existingSubmission.isPresent()) {
            throw new IllegalArgumentException("User has already submitted this assignment");
        }
        
        // Check if assignment is still open
        if (LocalDateTime.now().isAfter(assignment.getDueDate())) {
            throw new IllegalArgumentException("Assignment deadline has passed");
        }
        
        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setUser(user);
        submission.setContent(submissionDto.getContent());
        
        Submission savedSubmission = submissionRepository.save(submission);
        
        // Add to priority queue for processing
        submissionQueue.enqueue(savedSubmission);
        
        return savedSubmission;
    }
    
    public List<Submission> getSubmissionsByAssignment(Long assignmentId) {
        return submissionRepository.findByAssignmentId(assignmentId);
    }
    
    public List<Submission> getSubmissionsByUser(Long userId) {
        return submissionRepository.findByUserId(userId);
    }
    
    public Submission getSubmissionById(Long id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found with id: " + id));
    }
    
    public Submission gradeSubmission(Long submissionId, GradeSubmissionDto gradeDto) {
        Submission submission = getSubmissionById(submissionId);
        
        submission.setScore(gradeDto.getScore());
        submission.setFeedback(gradeDto.getFeedback());
        submission.setGraded(true);
        
        return submissionRepository.save(submission);
    }
    
    public List<Submission> getUngradedSubmissions(Long assignmentId) {
        return submissionRepository.findUngradedSubmissionsByAssignment(assignmentId);
    }
    
    public List<Submission> getGradedSubmissions(Long assignmentId) {
        return submissionRepository.findGradedSubmissionsByAssignment(assignmentId);
    }
    
    public Double getAverageScore(Long assignmentId) {
        return submissionRepository.getAverageScoreByAssignment(assignmentId);
    }
    
    public List<Assignment> getOverdueAssignments() {
        return assignmentRepository.findOverdueAssignments(LocalDateTime.now());
    }
    
    public List<Assignment> getAssignmentsByDateRange(LocalDateTime start, LocalDateTime end) {
        return assignmentRepository.findAssignmentsByDateRange(start, end);
    }
    
    public Submission getNextSubmissionToProcess() {
        if (submissionQueue.isEmpty()) {
            return null;
        }
        return submissionQueue.dequeue();
    }
    
    public List<Submission> getPendingSubmissions() {
        // This would need to be implemented based on your specific requirements
        // For now, returning empty list as we don't have a way to iterate through PriorityQueue
        return List.of();
    }
    
    public Map<String, Object> getAssignmentStatistics(Long assignmentId) {
        List<Submission> allSubmissions = submissionRepository.findByAssignmentId(assignmentId);
        List<Submission> gradedSubmissions = submissionRepository.findGradedSubmissionsByAssignment(assignmentId);
        Double averageScore = submissionRepository.getAverageScoreByAssignment(assignmentId);
        
        Map<String, Object> stats = Map.of(
            "totalSubmissions", allSubmissions.size(),
            "gradedSubmissions", gradedSubmissions.size(),
            "ungradedSubmissions", allSubmissions.size() - gradedSubmissions.size(),
            "averageScore", averageScore != null ? averageScore : 0.0
        );
        
        return stats;
    }
}