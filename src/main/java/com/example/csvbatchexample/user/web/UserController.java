package com.example.csvbatchexample.user.web;

import com.example.csvbatchexample.user.persistence.User;
import com.example.csvbatchexample.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public void save(@RequestBody List<User> users) {
        this.userService.save(users);
    }

    @GetMapping
    public List<User> getUsersFromDB() {
        return this.userService.getUsers();
    }
}
