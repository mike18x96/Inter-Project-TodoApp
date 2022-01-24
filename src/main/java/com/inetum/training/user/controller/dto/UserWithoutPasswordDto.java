package com.inetum.training.user.controller.dto;

import com.inetum.training.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserWithoutPasswordDto {

    public UserWithoutPasswordDto(User user){
        this.login = user.getLogin();
        this.role = user.getRole();
    }

    private String login;
    private User.Role role;
}
