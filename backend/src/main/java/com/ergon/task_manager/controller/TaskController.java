package com.ergon.task_manager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.ergon.task_manager.service.*;
import com.ergon.task_manager.model.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final CommentService commentService;

    public TaskController(TaskService taskService, CommentService commentService) {
        this.taskService = taskService;
        this.commentService = commentService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long task_id) {
        return taskService.getTaskById(task_id);
    }

    @GetMapping("user/{username}")
    public List<Task> getTasksByUser(@PathVariable String username) {
        return taskService.getTasksByUser(username);
    }

    @GetMapping("/{id}/total-hours")
    public Double getTaskTotalHours(@PathVariable Long taskId) {
        return taskService.getTotalHoursByTaskId(taskId);
    }

    @GetMapping("/{id}/comments")
    public List<Comment> getTaskComments(@PathVariable Long task_id) {
        return commentService.getCommentsByTaskId(task_id);
    }

    @PatchMapping("/{id}/status")
    public Task changeStatus(@PathVariable Long id, @RequestParam TaskStatus newStatus) {
        return taskService.updateTaskStatus(id, newStatus);
    }

    @PostMapping("/{id}/add-hours")
    public TaskAssignment addHours(@PathVariable Long id,
            @RequestParam String username,
            @RequestParam Double hours) {
        return taskService.addHours(id, username, hours);
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long task_id, @RequestBody Task task) {
        return taskService.updateTask(task_id, task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long task_id) {
        taskService.deleteTask(task_id);
    }

    @PostMapping("/{id}/assign")
    public TaskAssignment assignTask(@PathVariable Long task_id, @RequestParam String username) {
        return taskService.assignTask(username, task_id);
    }

}
