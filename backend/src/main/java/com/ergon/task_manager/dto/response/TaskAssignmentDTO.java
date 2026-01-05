package com.ergon.task_manager.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskAssignmentDTO {

    private String username;
    private LocalDateTime assignmentDate;
    private Double workedHours;
}