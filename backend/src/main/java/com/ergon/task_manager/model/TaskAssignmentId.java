package com.ergon.task_manager.model;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TaskAssignmentId implements Serializable {
    private Long task_id;
    private String user_id;
}