package com.ergon.task_manager.dto.mapper;

import org.springframework.stereotype.Component;
import com.ergon.task_manager.model.Comment;
import com.ergon.task_manager.dto.response.CommentResponseDTO;

@Component
public class CommentMapper {

    private final UserMapper userMapper;

    public CommentMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public CommentResponseDTO toDTO(Comment comments) {
        if (comments == null) {
            return null;
        }

        return CommentResponseDTO.builder()
                .id(comments.getId())
                .content(comments.getContent())
                .createdAt(comments.getCreatedAt())
                .authorUsername(comments.getUser().getUsername())
                .authorFullName(userMapper.getFullName(comments.getUser()))
                .build();
    }

}
