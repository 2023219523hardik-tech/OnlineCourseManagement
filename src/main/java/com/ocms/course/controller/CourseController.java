package com.ocms.course.controller;

import com.ocms.common.datastructures.LinkedList;
import com.ocms.common.dto.ApiResponse;
import com.ocms.course.dto.CourseDto;
import com.ocms.course.dto.ModuleDto;
import com.ocms.course.entity.Course;
import com.ocms.course.entity.CourseEnrollment;
import com.ocms.course.entity.Module;
import com.ocms.course.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    
    private final CourseService courseService;
    
    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Course>> createCourse(@Valid @RequestBody CourseDto courseDto) {
        Course course = courseService.createCourse(courseDto);
        return ResponseEntity.ok(ApiResponse.success("Course created successfully", course));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(ApiResponse.success(course));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Course>>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(ApiResponse.success(courses));
    }
    
    @GetMapping("/instructor/{instructorId}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Course>>> getCoursesByInstructor(@PathVariable Long instructorId) {
        List<Course> courses = courseService.getCoursesByInstructor(instructorId);
        return ResponseEntity.ok(ApiResponse.success(courses));
    }
    
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Course>>> getCoursesByStudent(@PathVariable Long studentId) {
        List<Course> courses = courseService.getCoursesByStudent(studentId);
        return ResponseEntity.ok(ApiResponse.success(courses));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Course>> updateCourse(@PathVariable Long id, 
                                                          @Valid @RequestBody CourseDto courseDto) {
        Course course = courseService.updateCourse(id, courseDto);
        return ResponseEntity.ok(ApiResponse.success("Course updated successfully", course));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(ApiResponse.success("Course deleted successfully", null));
    }
    
    @PostMapping("/modules")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Module>> addModule(@Valid @RequestBody ModuleDto moduleDto) {
        Module module = courseService.addModule(moduleDto);
        return ResponseEntity.ok(ApiResponse.success("Module added successfully", module));
    }
    
    @GetMapping("/{courseId}/modules")
    public ResponseEntity<ApiResponse<List<Module>>> getModulesByCourse(@PathVariable Long courseId) {
        List<Module> modules = courseService.getModulesByCourse(courseId);
        return ResponseEntity.ok(ApiResponse.success(modules));
    }
    
    @GetMapping("/modules/{id}")
    public ResponseEntity<ApiResponse<Module>> getModuleById(@PathVariable Long id) {
        Module module = courseService.getModuleById(id);
        return ResponseEntity.ok(ApiResponse.success(module));
    }
    
    @PutMapping("/modules/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Module>> updateModule(@PathVariable Long id, 
                                                          @Valid @RequestBody ModuleDto moduleDto) {
        Module module = courseService.updateModule(id, moduleDto);
        return ResponseEntity.ok(ApiResponse.success("Module updated successfully", module));
    }
    
    @DeleteMapping("/modules/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteModule(@PathVariable Long id) {
        courseService.deleteModule(id);
        return ResponseEntity.ok(ApiResponse.success("Module deleted successfully", null));
    }
    
    @PostMapping("/{courseId}/enroll/{studentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CourseEnrollment>> enrollStudent(@PathVariable Long courseId, 
                                                                      @PathVariable Long studentId) {
        CourseEnrollment enrollment = courseService.enrollStudent(courseId, studentId);
        return ResponseEntity.ok(ApiResponse.success("Student enrolled successfully", enrollment));
    }
    
    @GetMapping("/{courseId}/enrollments")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CourseEnrollment>>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        List<CourseEnrollment> enrollments = courseService.getEnrollmentsByCourse(courseId);
        return ResponseEntity.ok(ApiResponse.success(enrollments));
    }
    
    @GetMapping("/{courseId}/module-sequence")
    public ResponseEntity<ApiResponse<LinkedList<Module>>> getModuleSequence(@PathVariable Long courseId) {
        LinkedList<Module> sequence = courseService.getModuleSequence(courseId);
        return ResponseEntity.ok(ApiResponse.success(sequence));
    }
    
    @GetMapping("/module-sequences")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<Long, LinkedList<Module>>>> getCourseModuleSequences() {
        Map<Long, LinkedList<Module>> sequences = courseService.getCourseModuleSequences();
        return ResponseEntity.ok(ApiResponse.success(sequences));
    }
    
    @GetMapping("/enrollment-cache")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<Long, List<CourseEnrollment>>>> getCourseEnrollmentCache() {
        Map<Long, List<CourseEnrollment>> cache = courseService.getCourseEnrollmentCache();
        return ResponseEntity.ok(ApiResponse.success(cache));
    }
}