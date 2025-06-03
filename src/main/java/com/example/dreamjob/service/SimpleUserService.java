package com.example.dreamjob.service;

import com.example.dreamjob.dto.UserDto;
import com.example.dreamjob.exception.EntityAlreadyExistsException;
import com.example.dreamjob.exception.UserValidationException;
import com.example.dreamjob.model.User;
import com.example.dreamjob.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public User findByEmailAndPassword(UserDto dto) {
        Optional<User> user = userRepository.findByEmailAndPassword(dto.email(), dto.password());
        if (user.isEmpty()) {
            throw new UserValidationException("Почта или пароль введены неверно");
        }
        return user.get();
    }
}
