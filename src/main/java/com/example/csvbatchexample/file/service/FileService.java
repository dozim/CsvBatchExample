package com.example.csvbatchexample.file.service;

import com.example.csvbatchexample.user.persistence.User;
import com.example.csvbatchexample.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FileService {

    private final UserService userService;

    public List<User> readUsersFromFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("user.csv");
        ArrayList<User> users = new ArrayList<>();

        try {

            for (String line : Files.readAllLines(Path.of(resource.toURI()))) {
                String[] split = line.split(";");

                users.add(this.userService.map(split[0], split[1], split[2]));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return users;
    }
}
