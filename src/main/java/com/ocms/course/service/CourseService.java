package com.ocms.course.service;

import com.ocms.common.datastructures.LinkedList;
import com.ocms.common.exception.ResourceNotFoundException;
import com.ocms.course.dto.CourseDto;
import com.ocms.course.dto.ModuleDto;
import com.ocms.course.entity.Course;
import com.ocms.course.entity.CourseEnrollment;
import com.ocms.course.entity.Module;
import com.ocms.course.repository.CourseEnrollmentRepository;
import com.ocms.course.repository.CourseRepository;
import com.ocms.course.repository.ModuleRepository;
import com.ocms.user.entity.User;
import com.ocms.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CourseService {
    
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final CourseEnrollmentRepository enrollmentRepository;
    private final UserService userService;
    
    // HashMap for storing course enrollments
    private final Map<Long, List<CourseEnrollment>> courseEnrollmentCache = new HashMap<>();
    
    // LinkedList for managing course modules in sequence
    private final Map<Long, LinkedList<Module>> courseModuleSequences = new HashMap<>();
    
    public Course createCourse(CourseDto courseDto) {
        User instructor = userService.getUserById(courseDto.getInstructorId());
        
        Course course = new Course();
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setInstructor(instructor);
        
        Course savedCourse = courseRepository.save(course);
        
        // Initialize LinkedList for this course
        courseModuleSequences.put(savedCourse.getId(), new LinkedList<>());
        
        return savedCourse;
    }
    
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }
    
    public List<Course> getAllCourses() {
        return courseRepository.findByIsActiveTrue();
    }
    
    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findActiveCoursesByInstructor(instructorId);
    }
    
    public List<Course> getCoursesByStudent(Long studentId) {
        return courseRepository.findCoursesByStudent(studentId);
    }
    
    public Course updateCourse(Long id, CourseDto courseDto) {
        Course course = getCourseById(id);
        
        if (courseDto.getTitle() != null) {
            course.setTitle(courseDto.getTitle());
        }
        if (courseDto.getDescription() != null) {
            course.setDescription(courseDto.getDescription());
        }
        if (courseDto.getInstructorId() != null) {
            User instructor = userService.getUserById(courseDto.getInstructorId());
            course.setInstructor(instructor);
        }
        
        return courseRepository.save(course);
    }
    
    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        course.setActive(false);
        courseRepository.save(course);
        
        // Clear related caches
        courseModuleSequences.remove(id);
        courseEnrollmentCache.remove(id);
    }
    
    public Module addModule(ModuleDto moduleDto) {
        Course course = getCourseById(moduleDto.getCourseId());
        
        Module module = new Module();
        module.setModuleTitle(moduleDto.getModuleTitle());
        module.setContent(moduleDto.getContent());
        module.setModuleOrder(moduleDto.getModuleOrder());
        module.setCourse(course);
        
        Module savedModule = moduleRepository.save(module);
        
        // Add to LinkedList sequence
        LinkedList<Module> sequence = courseModuleSequences.get(course.getId());
        if (sequence == null) {
            sequence = new LinkedList<>();
            courseModuleSequences.put(course.getId(), sequence);
        }
        sequence.add(savedModule);
        
        return savedModule;
    }
    
    public List<Module> getModulesByCourse(Long courseId) {
        return moduleRepository.findModulesByCourseOrdered(courseId);
    }
    
    public Module getModuleById(Long id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + id));
    }
    
    public Module updateModule(Long id, ModuleDto moduleDto) {
        Module module = getModuleById(id);
        
        if (moduleDto.getModuleTitle() != null) {
            module.setModuleTitle(moduleDto.getModuleTitle());
        }
        if (moduleDto.getContent() != null) {
            module.setContent(moduleDto.getContent());
        }
        if (moduleDto.getModuleOrder() != null) {
            module.setModuleOrder(moduleDto.getModuleOrder());
        }
        
        return moduleRepository.save(module);
    }
    
    public void deleteModule(Long id) {
        Module module = getModuleById(id);
        Long courseId = module.getCourse().getId();
        
        moduleRepository.delete(module);
        
        // Remove from LinkedList sequence
        LinkedList<Module> sequence = courseModuleSequences.get(courseId);
        if (sequence != null) {
            // Rebuild sequence after deletion
            rebuildModuleSequence(courseId);
        }
    }
    
    public CourseEnrollment enrollStudent(Long courseId, Long studentId) {
        Course course = getCourseById(courseId);
        User student = userService.getUserById(studentId);
        
        // Check if already enrolled
        if (enrollmentRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }
        
        CourseEnrollment enrollment = new CourseEnrollment();
        enrollment.setCourse(course);
        enrollment.setStudent(student);
        
        CourseEnrollment savedEnrollment = enrollmentRepository.save(enrollment);
        
        // Update cache
        updateEnrollmentCache(courseId);
        
        return savedEnrollment;
    }
    
    public List<CourseEnrollment> getEnrollmentsByCourse(Long courseId) {
        // Check cache first
        List<CourseEnrollment> cached = courseEnrollmentCache.get(courseId);
        if (cached != null) {
            return cached;
        }
        
        // If not in cache, get from database
        List<CourseEnrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        courseEnrollmentCache.put(courseId, enrollments);
        
        return enrollments;
    }
    
    public LinkedList<Module> getModuleSequence(Long courseId) {
        LinkedList<Module> sequence = courseModuleSequences.get(courseId);
        if (sequence == null || sequence.isEmpty()) {
            // Rebuild sequence from database
            rebuildModuleSequence(courseId);
            sequence = courseModuleSequences.get(courseId);
        }
        return sequence;
    }
    
    private void rebuildModuleSequence(Long courseId) {
        List<Module> modules = moduleRepository.findModulesByCourseOrdered(courseId);
        LinkedList<Module> sequence = new LinkedList<>();
        
        for (Module module : modules) {
            sequence.add(module);
        }
        
        courseModuleSequences.put(courseId, sequence);
    }
    
    private void updateEnrollmentCache(Long courseId) {
        List<CourseEnrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        courseEnrollmentCache.put(courseId, enrollments);
    }
    
    public Map<Long, LinkedList<Module>> getCourseModuleSequences() {
        return new HashMap<>(courseModuleSequences);
    }
    
    public Map<Long, List<CourseEnrollment>> getCourseEnrollmentCache() {
        return new HashMap<>(courseEnrollmentCache);
    }
}