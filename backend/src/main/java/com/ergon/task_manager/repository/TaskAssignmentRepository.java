package com.ergon.task_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ergon.test.task_manager.model.TaskAssignment;
import com.ergon.test.task_manager.model.TaskAssignmentId;

@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, TaskAssignmentId> {

}