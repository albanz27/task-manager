package com.ergon.task_manager.service;

import java.util.List;

import com.ergon.task_manager.model.User;
import com.ergon.task_manager.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUsername(String username) {
        if (username == null)
            throw new IllegalArgumentException("Username cannot be null");

        return userRepository.findById(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User createUser(User user) {
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

    public User updateUser(String username, User updatedUser) {
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

    public void deleteUser(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        userRepository.deleteById(username);
    }

}
