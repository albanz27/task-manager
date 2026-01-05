package com.ergon.task_manager.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ergon.task_manager.service.*;

import jakarta.validation.Valid;

import com.ergon.task_manager.dto.mapper.CommentMapper;
import com.ergon.task_manager.dto.mapper.TaskMapper;
import com.ergon.task_manager.dto.request.TaskRequestDTO;
import com.ergon.task_manager.dto.response.CommentResponseDTO;
import com.ergon.task_manager.dto.response.TaskResponseDTO;
import com.ergon.task_manager.model.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final CommentService commentService;
    private final TaskMapper taskMapper;
    private final CommentMapper commentMapper;

    public TaskController(TaskService taskService, CommentService commentService, TaskMapper taskMapper,
            CommentMapper commentMapper) {
        this.taskService = taskService;
        this.commentService = commentService;
        this.taskMapper = taskMapper;
        this.commentMapper = commentMapper;
    }

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

    @PostMapping("/{id}/assign")
    public ResponseEntity<TaskResponseDTO> assignTask(@PathVariable Long task_id, @RequestParam String username) {
        taskService.assignTask(username, task_id);
        Task updated = taskService.getTaskById(task_id);
        return ResponseEntity.ok(taskMapper.toDTO(updated));
    }

    /*
     * @GetMapping("/{id}/assign")
     * public ResponseEntity<List<TaskResponseDTO>> getAssign(@RequestParam String
     * param) {
     * return new String();
     * }
     */

    @GetMapping("/user/{username}")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByUser(@PathVariable String username) {
        List<TaskResponseDTO> tasks = taskService.getTasksByUser(username).stream().map(taskMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}/total-hours")
    public ResponseEntity<Double> getTaskTotalHours(@PathVariable("id") Long taskId) {
        Double total = taskService.getTotalHoursByTaskId(taskId);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/{task_id}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getTaskComments(@PathVariable Long task_id) {
        List<CommentResponseDTO> comments = commentService.getCommentsByTaskId(task_id)
                .stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(comments);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponseDTO> changeStatus(@PathVariable Long task_id,
            @RequestParam TaskStatus newStatus) {
        Task updatedTask = taskService.updateTaskStatus(task_id, newStatus);
        return ResponseEntity.ok(taskMapper.toDTO(updatedTask));
    }

    @PostMapping("/{id}/add-hours")
    public ResponseEntity<TaskResponseDTO> addHours(@PathVariable Long id, @RequestParam String username,
            @RequestParam Double hours) {
        taskService.addHours(id, username, hours);
        Task updatedTask = taskService.getTaskById(id);
        return ResponseEntity.ok(taskMapper.toDTO(updatedTask));
    }

}
