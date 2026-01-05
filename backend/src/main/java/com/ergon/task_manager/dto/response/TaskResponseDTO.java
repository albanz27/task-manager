package com.ergon.task_manager.dto.response;

import lombok.*;
import com.ergon.task_manager.model.TaskStatus;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponseDTO {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Double totalHours;
    private List<TaskAssignmentDTO> assignments;
    private List<CommentResponseDTO> comments;
}