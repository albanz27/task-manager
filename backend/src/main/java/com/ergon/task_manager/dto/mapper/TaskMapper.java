package com.ergon.task_manager.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

import com.ergon.task_manager.dto.request.TaskRequestDTO;
import com.ergon.task_manager.dto.response.CommentResponseDTO;
import com.ergon.task_manager.dto.response.TaskAssignmentDTO;
import com.ergon.task_manager.dto.response.TaskResponseDTO;
import com.ergon.task_manager.model.Task;
import com.ergon.task_manager.model.TaskAssignment;
import com.ergon.task_manager.model.TaskStatus;

@Component
public class TaskMapper {

    private final CommentMapper commentMapper;

    public TaskMapper(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public TaskResponseDTO toDTO(Task task) {
        if (task == null)
            return null;

        List<TaskAssignmentDTO> assignmentDTOs = task.getAssignments() != null
                ? task.getAssignments().stream().map(this::toAssignmentDTO).collect(Collectors.toList())
                : new ArrayList<>();

        List<CommentResponseDTO> commentDTOs = (task.getComments() != null)
                ? task.getComments().stream().map(commentMapper::toDTO).collect(Collectors.toList())
                : new ArrayList<>();

        double totalHours = task.getAssignments() != null
                ? task.getAssignments().stream().mapToDouble(TaskAssignment::getWorkedHours).sum()
                : 0.0;

        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .totalHours(totalHours)
                .assignments(assignmentDTOs)
                .comments(commentDTOs)
                .build();
    }

    private TaskAssignmentDTO toAssignmentDTO(TaskAssignment assignment) {
        if (assignment == null)
            return null;
        return TaskAssignmentDTO.builder()
                .username(assignment.getUser().getUsername())
                .assignmentDate(assignment.getAssignmentDate())
                .workedHours(assignment.getWorkedHours())
                .build();
    }

    public Task toEntity(TaskRequestDTO dto) {
        if (dto == null)
            return null;
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus() != null ? dto.getStatus() : TaskStatus.BACKLOG);
        return task;
    }
}