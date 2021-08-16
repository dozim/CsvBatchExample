package com.example.csvbatchexample.user.mapper;

import com.example.csvbatchexample.user.persistence.User;
import com.example.csvbatchexample.user.web.UserDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapUserFromUserDTO(UserDTO userDTO);

    UserDTO mapUserDTOFromUser(User user);

    User map(String vorname, String nachname, String alter);
}
