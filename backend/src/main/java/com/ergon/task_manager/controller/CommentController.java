package com.ergon.task_manager.controller;

import org.springframework.web.bind.annotation.*;

import com.ergon.task_manager.model.Comment;
import com.ergon.task_manager.service.CommentService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public Comment addComment(@PathVariable Long task_id,
            @RequestParam String user_id,
            @RequestParam String content) {
        return commentService.createComment(user_id, task_id, content);
    }

    @GetMapping("/{id}")
    public Comment getComment(@RequestParam Long id) {
        return commentService.getComment(id);
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable Long id, @RequestBody String comment) {
        return commentService.updateComment(id, comment);
    }

    @DeleteMapping("{id}")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }

}
