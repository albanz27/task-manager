package com.ergon.task_manager.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignment {

    @EmbeddedId
    private TaskAssignmentId id;

    @ManyToOne
    @MapsId("task_id")
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime assignmentDate = LocalDateTime.now();

    private Double workedHours = 0.0;
}