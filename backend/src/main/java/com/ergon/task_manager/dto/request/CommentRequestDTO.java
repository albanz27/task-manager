package com.ergon.task_manager.dto.request;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDTO {

    @NotBlank(message = "content cannot be blank")
    @Size(max = 2000, message = "comment content cannot exceed 2000 characters")
    private String content;

    @NotBlank(message = "authorUsername cannot be blank")
    private String authorUsername;

}
