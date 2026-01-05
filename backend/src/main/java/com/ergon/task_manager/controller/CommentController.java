package com.ergon.task_manager.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.ergon.task_manager.dto.mapper.CommentMapper;
import com.ergon.task_manager.dto.request.CommentRequestDTO;
import com.ergon.task_manager.dto.response.CommentResponseDTO;
import com.ergon.task_manager.model.Comment;
import com.ergon.task_manager.service.CommentService;

@RestController
@RequestMapping("/api/tasks/{task_id}/comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    public CommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> getTaskComments(@PathVariable Long task_id) {
        List<CommentResponseDTO> comments = commentService.getCommentsByTaskId(task_id)
                .stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentResponseDTO> addComment(
            @PathVariable Long task_id,
            @Valid @RequestBody CommentRequestDTO dto) {

        Comment comment = commentService.createComment(
                dto.getAuthorUsername(),
                task_id,
                dto.getContent());

        return ResponseEntity.status(HttpStatus.CREATED).body(commentMapper.toDTO(comment));
    }

    @GetMapping("/{comment_id}")
    public ResponseEntity<CommentResponseDTO> getComment(
            @PathVariable Long task_id,
            @PathVariable Long comment_id) {

        Comment comment = commentService.getComment(comment_id);
        return ResponseEntity.ok(commentMapper.toDTO(comment));
    }

    @PutMapping("/{comment_id}")
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable Long comment_id,
            @Valid @RequestBody CommentRequestDTO dto) {
        Comment updatedComment = commentService.updateComment(comment_id, dto.getContent());
        return ResponseEntity.ok(commentMapper.toDTO(updatedComment));
    }

    @DeleteMapping("/{comment_id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long comment_id) {
        commentService.deleteComment(comment_id);
        return ResponseEntity.noContent().build();
    }
}