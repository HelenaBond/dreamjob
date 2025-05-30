package com.example.dreamjob.repository;

import com.example.dreamjob.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);

    Collection<User> findAll();

    void deleteById(int id);

    Optional<User> findById(int id);
}
