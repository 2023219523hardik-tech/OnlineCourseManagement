package com.ocms.reporting.service;

import com.ocms.assignment.entity.Assignment;
import com.ocms.assignment.entity.Submission;
import com.ocms.assignment.service.AssignmentService;
import com.ocms.course.entity.Course;
import com.ocms.course.entity.CourseEnrollment;
import com.ocms.course.service.CourseService;
import com.ocms.reporting.dto.ReportDto;
import com.ocms.user.entity.User;
import com.ocms.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportingService {
    
    private final CourseService courseService;
    private final AssignmentService assignmentService;
    private final UserService userService;
    
    // TreeMap for generating reports sorted by date
    private final TreeMap<LocalDateTime, Object> activityLog = new TreeMap<>();
    
    public Map<String, Object> generateStudentPerformanceReport(Long studentId, LocalDateTime startDate, LocalDateTime endDate) {
        User student = userService.getUserById(studentId);
        List<Submission> submissions = assignmentService.getSubmissionsByUser(studentId);
        
        // Filter submissions by date range
        List<Submission> filteredSubmissions = submissions.stream()
                .filter(s -> s.getSubmittedOn().isAfter(startDate) && s.getSubmittedOn().isBefore(endDate))
                .collect(Collectors.toList());
        
        // Calculate statistics
        double averageScore = filteredSubmissions.stream()
                .filter(Submission::isGraded)
                .mapToDouble(Submission::getScore)
                .average()
                .orElse(0.0);
        
        long totalAssignments = filteredSubmissions.size();
        long gradedAssignments = filteredSubmissions.stream()
                .filter(Submission::isGraded)
                .count();
        
        Map<String, Object> report = new HashMap<>();
        report.put("studentId", studentId);
        report.put("studentName", student.getFirstName() + " " + student.getLastName());
        report.put("totalAssignments", totalAssignments);
        report.put("gradedAssignments", gradedAssignments);
        report.put("averageScore", averageScore);
        report.put("completionRate", totalAssignments > 0 ? (double) gradedAssignments / totalAssignments * 100 : 0.0);
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        
        // Add to activity log
        activityLog.put(LocalDateTime.now(), "Student Performance Report Generated for " + student.getUsername());
        
        return report;
    }
    
    public Map<String, Object> generateCourseCompletionReport(Long courseId) {
        Course course = courseService.getCourseById(courseId);
        List<CourseEnrollment> enrollments = courseService.getEnrollmentsByCourse(courseId);
        
        long totalEnrollments = enrollments.size();
        long completedEnrollments = enrollments.stream()
                .filter(e -> e.getStatus() == CourseEnrollment.EnrollmentStatus.COMPLETED)
                .count();
        
        double averageCompletionPercentage = enrollments.stream()
                .mapToDouble(CourseEnrollment::getCompletionPercentage)
                .average()
                .orElse(0.0);
        
        Map<String, Object> report = new HashMap<>();
        report.put("courseId", courseId);
        report.put("courseTitle", course.getTitle());
        report.put("totalEnrollments", totalEnrollments);
        report.put("completedEnrollments", completedEnrollments);
        report.put("completionRate", totalEnrollments > 0 ? (double) completedEnrollments / totalEnrollments * 100 : 0.0);
        report.put("averageCompletionPercentage", averageCompletionPercentage);
        
        // Add to activity log
        activityLog.put(LocalDateTime.now(), "Course Completion Report Generated for " + course.getTitle());
        
        return report;
    }
    
    public Map<String, Object> generateAssignmentStatisticsReport(Long assignmentId) {
        return assignmentService.getAssignmentStatistics(assignmentId);
    }
    
    public Map<String, Object> generateSystemActivityReport(LocalDateTime startDate, LocalDateTime endDate) {
        // Get activities within date range
        NavigableMap<LocalDateTime, Object> activitiesInRange = activityLog.subMap(startDate, true, endDate, true);
        
        Map<String, Object> report = new HashMap<>();
        report.put("totalActivities", activitiesInRange.size());
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        report.put("activities", new ArrayList<>(activitiesInRange.values()));
        
        // Group activities by date
        Map<String, Long> activitiesByDate = activitiesInRange.entrySet().stream()
                .collect(Collectors.groupingBy(
                    entry -> entry.getKey().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    Collectors.counting()
                ));
        
        report.put("activitiesByDate", activitiesByDate);
        
        return report;
    }
    
    public Map<String, Object> generateUserActivityReport(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        User user = userService.getUserById(userId);
        List<Submission> submissions = assignmentService.getSubmissionsByUser(userId);
        
        // Filter submissions by date range
        List<Submission> filteredSubmissions = submissions.stream()
                .filter(s -> s.getSubmittedOn().isAfter(startDate) && s.getSubmittedOn().isBefore(endDate))
                .collect(Collectors.toList());
        
        // Group submissions by date
        Map<String, Long> submissionsByDate = filteredSubmissions.stream()
                .collect(Collectors.groupingBy(
                    s -> s.getSubmittedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    Collectors.counting()
                ));
        
        Map<String, Object> report = new HashMap<>();
        report.put("userId", userId);
        report.put("userName", user.getFirstName() + " " + user.getLastName());
        report.put("totalSubmissions", filteredSubmissions.size());
        report.put("submissionsByDate", submissionsByDate);
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        
        return report;
    }
    
    public Map<String, Object> generateInstructorPerformanceReport(Long instructorId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Course> courses = courseService.getCoursesByInstructor(instructorId);
        
        Map<String, Object> report = new HashMap<>();
        report.put("instructorId", instructorId);
        report.put("totalCourses", courses.size());
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        
        // Calculate course statistics
        List<Map<String, Object>> courseStats = new ArrayList<>();
        for (Course course : courses) {
            List<CourseEnrollment> enrollments = courseService.getEnrollmentsByCourse(course.getId());
            long totalEnrollments = enrollments.size();
            long completedEnrollments = enrollments.stream()
                    .filter(e -> e.getStatus() == CourseEnrollment.EnrollmentStatus.COMPLETED)
                    .count();
            
            Map<String, Object> courseStat = new HashMap<>();
            courseStat.put("courseId", course.getId());
            courseStat.put("courseTitle", course.getTitle());
            courseStat.put("totalEnrollments", totalEnrollments);
            courseStat.put("completedEnrollments", completedEnrollments);
            courseStat.put("completionRate", totalEnrollments > 0 ? (double) completedEnrollments / totalEnrollments * 100 : 0.0);
            
            courseStats.add(courseStat);
        }
        
        report.put("courseStatistics", courseStats);
        
        return report;
    }
    
    public TreeMap<LocalDateTime, Object> getActivityLog() {
        return new TreeMap<>(activityLog);
    }
    
    public void logActivity(String activity) {
        activityLog.put(LocalDateTime.now(), activity);
    }
    
    public Map<String, Object> generateCustomReport(ReportDto reportDto) {
        switch (reportDto.getReportType().toLowerCase()) {
            case "student_performance":
                return generateStudentPerformanceReport(
                    reportDto.getUserId(), 
                    reportDto.getStartDate(), 
                    reportDto.getEndDate()
                );
            case "course_completion":
                return generateCourseCompletionReport(reportDto.getCourseId());
            case "assignment_statistics":
                return generateAssignmentStatisticsReport(reportDto.getCourseId());
            case "system_activity":
                return generateSystemActivityReport(
                    reportDto.getStartDate(), 
                    reportDto.getEndDate()
                );
            case "user_activity":
                return generateUserActivityReport(
                    reportDto.getUserId(), 
                    reportDto.getStartDate(), 
                    reportDto.getEndDate()
                );
            case "instructor_performance":
                return generateInstructorPerformanceReport(
                    reportDto.getUserId(), 
                    reportDto.getStartDate(), 
                    reportDto.getEndDate()
                );
            default:
                throw new IllegalArgumentException("Unknown report type: " + reportDto.getReportType());
        }
    }
}