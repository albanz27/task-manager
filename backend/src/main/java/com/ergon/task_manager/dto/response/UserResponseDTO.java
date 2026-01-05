package com.ergon.task_manager.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private String username;
    private String mail;
    private String name;
    private String surname;

}