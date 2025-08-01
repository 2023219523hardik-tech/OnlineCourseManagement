package com.ocms.assignment.repository;

import com.ocms.assignment.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    
    List<Assignment> findByCourseId(Long courseId);
    
    List<Assignment> findByCourseIdAndIsActiveTrue(Long courseId);
    
    @Query("SELECT a FROM Assignment a WHERE a.dueDate < :now AND a.isActive = true")
    List<Assignment> findOverdueAssignments(@Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Assignment a WHERE a.dueDate BETWEEN :start AND :end AND a.isActive = true")
    List<Assignment> findAssignmentsByDateRange(@Param("start") LocalDateTime start, 
                                               @Param("end") LocalDateTime end);
}