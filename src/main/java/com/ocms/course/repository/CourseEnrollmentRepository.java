package com.ocms.course.repository;

import com.ocms.course.entity.CourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Long> {
    
    List<CourseEnrollment> findByCourseId(Long courseId);
    
    List<CourseEnrollment> findByStudentId(Long studentId);
    
    boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);
    
    @Query("SELECT ce FROM CourseEnrollment ce WHERE ce.course.id = :courseId AND ce.status = :status")
    List<CourseEnrollment> findByCourseIdAndStatus(@Param("courseId") Long courseId, 
                                                   @Param("status") CourseEnrollment.EnrollmentStatus status);
    
    @Query("SELECT COUNT(ce) FROM CourseEnrollment ce WHERE ce.course.id = :courseId")
    long countByCourseId(@Param("courseId") Long courseId);
}