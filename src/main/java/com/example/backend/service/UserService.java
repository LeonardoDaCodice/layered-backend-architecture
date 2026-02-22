package com.example.backend.service;

import com.example.backend.dto.UserRequestDTO;
import com.example.backend.dto.UserResponseDTO;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {

        log.debug("Fetching user with id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with id={}", id);
                    return new UserNotFoundException(id);
                });

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getAge()
        );
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO request) {

        log.info("Creating user with name={} and age={}", request.getName(), request.getAge());

        User user = new User(request.getName(), request.getAge());
        User saved = userRepository.save(user);

        log.info("User created successfully with id={}", saved.getId());

        return new UserResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getAge()
        );
    }

    @Transactional
    public void deleteUser(Long id) {

        log.info("Deleting user with id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attempted to delete non-existing user with id={}", id);
                    return new UserNotFoundException(id);
                });

        userRepository.delete(user);

        log.info("User deleted successfully with id={}", id);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO request) {

        log.info("Updating user with id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attempted to update non-existing user with id={}", id);
                    return new UserNotFoundException(id);
                });

        user.setName(request.getName());
        user.setAge(request.getAge());

        User updated = userRepository.save(user);

        log.info("User updated successfully with id={}", id);

        return new UserResponseDTO(
                updated.getId(),
                updated.getName(),
                updated.getAge()
        );
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> searchByName(String name) {

        return userRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(u -> new UserResponseDTO(
                        u.getId(),
                        u.getName(),
                        u.getAge()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(u -> new UserResponseDTO(
                        u.getId(),
                        u.getName(),
                        u.getAge()
                ))
                .toList();
    }
}