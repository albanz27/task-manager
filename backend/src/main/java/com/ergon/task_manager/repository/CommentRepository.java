package com.ergon.task_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ergon.test.task_manager.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}