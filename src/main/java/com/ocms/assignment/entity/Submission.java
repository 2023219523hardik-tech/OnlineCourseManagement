package com.ocms.assignment.entity;

import com.ocms.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Submission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "submitted_on", nullable = false)
    private LocalDateTime submittedOn;
    
    @Column(name = "score")
    private Double score;
    
    @Column(name = "feedback")
    private String feedback;
    
    @Column(name = "is_graded")
    private boolean isGraded = false;
    
    @PrePersist
    protected void onCreate() {
        submittedOn = LocalDateTime.now();
    }
}