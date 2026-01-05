package com.ergon.task_manager.dto.mapper;

import org.springframework.stereotype.Component;

import com.ergon.task_manager.dto.request.UserRequestDTO;
import com.ergon.task_manager.dto.response.UserResponseDTO;
import com.ergon.task_manager.model.User;

@Component
public class UserMapper {

    public UserResponseDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserResponseDTO.builder()
                .username(user.getUsername())
                .mail(user.getMail())
                .name(user.getName())
                .surname(user.getSurname())
                .build();
    }

    public User toEntity(UserRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setMail(dto.getMail());
        user.setPassword(dto.getPassword());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());

        return user;
    }

    public String getFullName(User user) {
        if (user == null) {
            return "";
        }

        String name = user.getName() != null ? user.getName() : "";
        String surname = user.getSurname() != null ? user.getSurname() : "";

        if (name.isEmpty() && surname.isEmpty()) {
            return user.getUsername();
        }

        return (name + " " + surname).trim();
    }

}
