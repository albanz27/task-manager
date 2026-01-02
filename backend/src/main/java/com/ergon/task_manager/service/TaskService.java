package com.ergon.task_manager.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ergon.task_manager.repository.*;
import com.ergon.task_manager.model.*;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, TaskAssignmentRepository taskAssignmentRepository,
            UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public TaskAssignment assignTask(String user_id, Long task_id) {
        Task task = taskRepository.findById(task_id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TaskAssignmentId id = new TaskAssignmentId(task_id, user_id);

        TaskAssignment assignment = new TaskAssignment();
        assignment.setId(id);
        assignment.setTask(task);
        assignment.setUser(user);
        assignment.setAssignmentDate(LocalDateTime.now());
        assignment.setWorkedHours(0.0);

        return taskAssignmentRepository.save(assignment);
    }

    public double getTotalHoursByTaskId(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("task not found"));

        return task.getAssignments().stream()
                .mapToDouble(TaskAssignment::getWorkedHours)
                .sum();
    }

    public Task updateTaskStatus(Long task_id, TaskStatus status) {
        Task task = taskRepository.findById(task_id).orElseThrow(() -> new RuntimeException("task not found"));
        task.setStatus(status);
        return taskRepository.save(task);
    }

    public TaskAssignment addHours(Long task_id, String username, Double hours) {
        TaskAssignmentId id = new TaskAssignmentId(task_id, username);
        TaskAssignment assignment = taskAssignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User is not assigned to this task"));
        assignment.setWorkedHours(assignment.getWorkedHours() + hours);

        return taskAssignmentRepository.save(assignment);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

}
