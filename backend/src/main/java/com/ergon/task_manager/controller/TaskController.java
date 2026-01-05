package com.ergon.task_manager.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ergon.task_manager.service.*;

import jakarta.validation.Valid;

import com.ergon.task_manager.dto.mapper.TaskMapper;
import com.ergon.task_manager.dto.request.TaskRequestDTO;
import com.ergon.task_manager.dto.response.TaskAssignmentDTO;
import com.ergon.task_manager.dto.response.TaskResponseDTO;
import com.ergon.task_manager.model.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    // CRUD TASK

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO request,
            @RequestParam String createdBy) {

        Task task = taskMapper.toEntity(request);
        Task savedTask = taskService.createTask(task, createdBy);
        return ResponseEntity.ok(taskMapper.toDTO(savedTask));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        List<TaskResponseDTO> tasks = taskService.getAllTasks().stream().map(taskMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{task_id}")
    public ResponseEntity<TaskResponseDTO> getTask(@PathVariable Long task_id) {
        Task task = taskService.getTaskById(task_id);
        return ResponseEntity.ok(taskMapper.toDTO(task));
    }

    @PutMapping("/{task_id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long task_id,
            @Valid @RequestBody TaskRequestDTO dto) {
        Task task = taskMapper.toEntity(dto);
        Task updated = taskService.updateTask(task_id, task);
        return ResponseEntity.ok(taskMapper.toDTO(updated));
    }

    @DeleteMapping("/{task_id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long task_id) {
        taskService.deleteTask(task_id);
        return ResponseEntity.noContent().build();
    }

    // Assignment

    @PostMapping("/{task_id}/assign")
    public ResponseEntity<TaskResponseDTO> assignTask(@PathVariable Long task_id, @RequestParam String username) {
        taskService.addAssignment(username, task_id);
        Task updated = taskService.getTaskById(task_id);
        return ResponseEntity.ok(taskMapper.toDTO(updated));
    }

    @GetMapping("/{task_id}/assign")
    public ResponseEntity<TaskAssignmentDTO> getAssignment(@PathVariable Long task_id,
            @RequestParam String username) {
        TaskAssignment assignment = taskService.getAssignment(task_id, username);
        TaskAssignmentDTO dto = taskMapper.toAssignmentDTO(assignment);

        return ResponseEntity.ok(dto);

    }

    @DeleteMapping("/{task_id}/assign")
    public ResponseEntity<TaskResponseDTO> deleteTaskAssignment(@PathVariable Long task_id,
            @RequestParam String username) {
        taskService.deleteAssignment(task_id, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByUser(@PathVariable String username) {
        List<TaskResponseDTO> tasks = taskService.getTasksByUser(username).stream().map(taskMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{task_id}/total-hours")
    public ResponseEntity<Double> getTaskTotalHours(@PathVariable Long task_id) {
        Double total = taskService.getTotalHoursByTaskId(task_id);
        return ResponseEntity.ok(total);
    }

    @PatchMapping("/{task_id}/status")
    public ResponseEntity<TaskResponseDTO> changeStatus(@PathVariable Long task_id,
            @RequestParam TaskStatus newStatus) {
        Task updatedTask = taskService.updateTaskStatus(task_id, newStatus);
        return ResponseEntity.ok(taskMapper.toDTO(updatedTask));
    }

    @PostMapping("/{task_id}/add-hours")
    public ResponseEntity<TaskResponseDTO> addHours(@PathVariable Long task_id, @RequestParam String username,
            @RequestParam Double hours) {
        taskService.addHours(task_id, username, hours);
        Task updatedTask = taskService.getTaskById(task_id);
        return ResponseEntity.ok(taskMapper.toDTO(updatedTask));
    }

}
