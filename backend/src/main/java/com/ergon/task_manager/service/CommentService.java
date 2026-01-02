package com.ergon.task_manager.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ergon.task_manager.model.*;
import com.ergon.task_manager.repository.*;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private TaskRepository taskRepository;

    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository,
            UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Comment addComment(String user_id, Long task_id, String content) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        Task task = taskRepository.findById(task_id).orElseThrow(() -> new RuntimeException("Task not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setTask(task);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByTaskId(Long task_id) {
        Task task = taskRepository.findById(task_id).orElseThrow(() -> new RuntimeException("Task not found"));
        return task.getComments();
    }
}
