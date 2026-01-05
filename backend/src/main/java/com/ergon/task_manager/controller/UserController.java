package com.ergon.task_manager.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ergon.task_manager.service.UserService;

import jakarta.validation.Valid;

import com.ergon.task_manager.dto.mapper.UserMapper;
import com.ergon.task_manager.dto.request.UserRequestDTO;
import com.ergon.task_manager.dto.response.UserResponseDTO;
import com.ergon.task_manager.model.User;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers().stream().map(userMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);
        User saved = userService.createUser(user);
        return ResponseEntity.ok(userMapper.toDTO(saved));
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String username, @RequestBody UserRequestDTO dto) {
        User userUpdate = userMapper.toEntity(dto);
        User updated = userService.updateUser(username, userUpdate);
        return ResponseEntity.ok(userMapper.toDTO(updated));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

}
