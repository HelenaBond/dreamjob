package com.example.dreamjob.service;

import com.example.dreamjob.exception.EntityAlreadyExistsException;
import com.example.dreamjob.model.User;
import com.example.dreamjob.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class SimpleUserService implements UserService {

    private final UserRepository userRepository;

    public SimpleUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EntityAlreadyExistsException(
                    "Пользователь с почтой %s уже существует".formatted(user.getEmail()));
        }
        userRepository.save(user);
    }
}
