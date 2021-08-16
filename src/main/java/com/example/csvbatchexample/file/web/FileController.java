package com.example.csvbatchexample.file.web;

import com.example.csvbatchexample.file.service.FileService;
import com.example.csvbatchexample.user.persistence.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {

    public final FileService fileService;

    @GetMapping
    public List<User> getUserFromFile() {
        return fileService.readUsersFromFile();
    }


}
