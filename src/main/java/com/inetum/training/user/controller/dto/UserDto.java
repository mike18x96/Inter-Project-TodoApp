package com.inetum.training.user.controller.dto;

import com.inetum.training.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {

    private String login;
    private User.Role role;

    public UserDto(User user) {
        login = user.getLogin();
        role = user.getRole();
    }
}
