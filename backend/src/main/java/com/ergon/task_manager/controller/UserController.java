package com.ergon.task_manager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import com.ergon.task_manager.repository.UserRepository;
import com.ergon.task_manager.model.User;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("{username}")
    public User getUserByUsername(@PathVariable String username) {
        if (username == null)
            throw new IllegalArgumentException("Username cannot be null");

        return userRepository.findById(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        String username = user.getUsername();

        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }

        if (userRepository.existsById(username)) {
            throw new RuntimeException("Username already exsists");
        }

        if (userRepository.findByMail(user.getMail()) != null) {
            throw new RuntimeException("Email already exsists");
        }

        return userRepository.save(user);
    }

    @PutMapping("/{username}")
    public User updateUser(@PathVariable String username, @RequestBody User updatedUser) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        User user = userRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(updatedUser.getName());
        user.setSurname(updatedUser.getSurname());
        user.setMail(updatedUser.getMail());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(updatedUser.getPassword());
        }

        return userRepository.save(user);
    }

    @DeleteMapping("{username}")
    public void deleteUser(@PathVariable String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        if (!userRepository.existsById(username)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(username);
    }

}
