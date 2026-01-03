package com.ergon.task_manager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import com.ergon.task_manager.service.UserService;
import com.ergon.task_manager.model.User;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{username}")
    public User updateUser(@PathVariable String username, @RequestBody User updatedUser) {
        return userService.updateUser(username, updatedUser);
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
    }

}
