package com.ocms.course.repository;

import com.ocms.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    List<Course> findByInstructorId(Long instructorId);
    
    List<Course> findByIsActiveTrue();
    
    @Query("SELECT c FROM Course c WHERE c.instructor.id = :instructorId AND c.isActive = true")
    List<Course> findActiveCoursesByInstructor(@Param("instructorId") Long instructorId);
    
    @Query("SELECT c FROM Course c JOIN c.enrollments e WHERE e.student.id = :studentId AND c.isActive = true")
    List<Course> findCoursesByStudent(@Param("studentId") Long studentId);
}