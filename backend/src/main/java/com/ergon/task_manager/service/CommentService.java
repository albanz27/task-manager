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

    public Comment createComment(String user_id, Long task_id, String content) {
        if (task_id == null || user_id == null) {
            throw new IllegalArgumentException("task_id or user_id cannot be null");
        }
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        Task task = taskRepository.findById(task_id).orElseThrow(() -> new RuntimeException("Task not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setTask(task);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public Comment getComment(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        return commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    public Comment updateComment(Long id, String newContent) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        ;
        comment.setContent(newContent);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        commentRepository.deleteById(id);
    }

    public List<Comment> getCommentsByTaskId(Long task_id) {
        if (task_id == null) {
            throw new IllegalArgumentException("task_id cannot be null");
        }
        Task task = taskRepository.findById(task_id).orElseThrow(() -> new RuntimeException("Task not found"));
        return task.getComments();
    }
}
