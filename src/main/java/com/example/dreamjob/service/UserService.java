package com.example.dreamjob.service;

import com.example.dreamjob.dto.UserDto;
import com.example.dreamjob.model.User;

public interface UserService {
    void save(User user);

    User findByEmailAndPassword(UserDto dto);
}
