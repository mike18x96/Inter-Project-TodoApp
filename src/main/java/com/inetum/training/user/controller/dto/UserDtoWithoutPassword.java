package com.inetum.training.user.controller.dto;

import com.inetum.training.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserDtoWithoutPassword {

    public UserDtoWithoutPassword(User user){
        this.login = user.getLogin();
        this.role = user.getRole();
    }

    private String login;
    private User.Role role;
}
