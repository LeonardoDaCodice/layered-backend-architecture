package com.example.backend.repository;

import com.example.backend.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {

    private final Map<Long, User> database = new HashMap<>();
    private Long idCounter = 1L;

    public User save(User user) {
        User saved = new User(idCounter++, user.getName(), user.getAge());
        database.put(saved.getId(), saved);
        return saved;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }
}