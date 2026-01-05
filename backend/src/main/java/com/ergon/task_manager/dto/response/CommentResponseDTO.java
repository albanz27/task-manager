package com.ergon.task_manager.dto.response;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;

    private String authorUsername;
    private String authorFullName;
}
