package com.ocms.course.repository;

import com.ocms.course.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    
    List<Module> findByCourseIdOrderByModuleOrder(Long courseId);
    
    @Query("SELECT m FROM Module m WHERE m.course.id = :courseId ORDER BY m.moduleOrder")
    List<Module> findModulesByCourseOrdered(@Param("courseId") Long courseId);
    
    @Query("SELECT MAX(m.moduleOrder) FROM Module m WHERE m.course.id = :courseId")
    Integer findMaxModuleOrderByCourse(@Param("courseId") Long courseId);
}