package com.example.csvbatchexample.user.service;

import com.example.csvbatchexample.user.mapper.UserMapper;
import com.example.csvbatchexample.user.persistence.User;
import com.example.csvbatchexample.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public void save(User user) {
        this.userRepository.save(user);
    }

    public void save(List<User> users) {
        users.forEach(this::save);
    }

    public User map(String vorname, String nachname, String alter) {
        return this.userMapper.map(vorname, nachname, alter);
    }
}
