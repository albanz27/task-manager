package com.ergon.task_manager.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.BACKLOG;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskAssignment> assignments = new ArrayList<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @OrderBy("createdAt DESC")
    private List<Comment> comments = new ArrayList<>();

    public Double getTotalWorkedHours() {
        return assignments.stream()
                .mapToDouble(TaskAssignment::getWorkedHours)
                .sum();
    }

}