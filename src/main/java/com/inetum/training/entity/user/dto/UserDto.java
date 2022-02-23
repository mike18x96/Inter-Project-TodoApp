package com.inetum.training.entity.user.dto;

import com.inetum.training.entity.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {

    private Long id;
    private String login;
    private User.Role role;

    public UserDto(User user) {
        id = user.getId();
        login = user.getLogin();
        role = user.getRole();
    }

}
