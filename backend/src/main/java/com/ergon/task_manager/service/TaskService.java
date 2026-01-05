package com.ergon.task_manager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public Task createTask(Task task, String createdBy) {
        if (createdBy == null) {
            throw new IllegalArgumentException("createdBy cannot be null");
        }
        if (task == null) {
            throw new IllegalArgumentException("task cannot be null");
        }

        User creator = userRepository.findById(createdBy).orElseThrow(() -> new RuntimeException("User not found: "));
        Task savedTask = taskRepository.save(task);
        TaskAssignmentId assignmentId = new TaskAssignmentId(savedTask.getId(), createdBy);
        TaskAssignment assignment = new TaskAssignment();
        assignment.setId(assignmentId);
        assignment.setTask(savedTask);
        assignment.setUser(creator);
        assignment.setAssignmentDate(LocalDateTime.now());
        assignment.setWorkedHours(0.0);
        taskAssignmentRepository.save(assignment);

        Long savedTaskId = savedTask.getId();
        if (savedTaskId == null) {
            throw new IllegalStateException("task_id is null");
        }
        return taskRepository.findById(savedTaskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long task_id) {
        if (task_id == null) {
            throw new IllegalArgumentException("task_id cannot be null");
        }
        return taskRepository.findById(task_id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public Task updateTask(Long task_id, Task updatedTask) {
        if (task_id == null) {
            throw new IllegalArgumentException("task_id cannot be null");
        }
        Task task = getTaskById(task_id);
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        return taskRepository.save(task);
    }

    public void deleteTask(Long task_id) {
        if (task_id == null) {
            throw new IllegalArgumentException("task_id cannot be null");
        }
        taskRepository.deleteById(task_id);
    }

    public TaskAssignment assignTask(String user_id, Long task_id) {
        if (user_id == null) {
            throw new IllegalArgumentException("user_id cannot be null");
        }
        Task task = getTaskById(task_id);
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TaskAssignmentId id = new TaskAssignmentId(task_id, user_id);

        if (taskAssignmentRepository.existsById(id)) {
            throw new RuntimeException("User is already assigned to this task");
        }

        TaskAssignment assignment = new TaskAssignment();
        assignment.setId(id);
        assignment.setTask(task);
        assignment.setUser(user);
        assignment.setAssignmentDate(LocalDateTime.now());
        assignment.setWorkedHours(0.0);

        return taskAssignmentRepository.save(assignment);
    }

    public Task updateTaskStatus(Long task_id, TaskStatus status) {
        Task task = getTaskById(task_id);
        task.setStatus(status);
        return taskRepository.save(task);
    }

    public List<Task> getTasksByUser(String username) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getAssignments().stream()
                        .anyMatch(assignment -> assignment.getUser().getUsername().equals(username)))
                .collect(Collectors.toList());
    }

    public double getTotalHoursByTaskId(Long task_id) {
        if (task_id == null) {
            throw new IllegalArgumentException("task_id cannot be null");
        }
        Task task = taskRepository.findById(task_id).orElseThrow(() -> new RuntimeException("task not found"));

        return task.getAssignments().stream()
                .mapToDouble(TaskAssignment::getWorkedHours)
                .sum();
    }

    public TaskAssignment addHours(Long task_id, String username, Double hours) {
        TaskAssignmentId id = new TaskAssignmentId(task_id, username);
        TaskAssignment assignment = taskAssignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User is not assigned to this task"));
        assignment.setWorkedHours(assignment.getWorkedHours() + hours);

        return taskAssignmentRepository.save(assignment);
    }

}
