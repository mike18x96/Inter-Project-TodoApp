package com.inetum.training.security;

import com.inetum.training.entity.user.dto.UserDto;
import com.inetum.training.entity.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CurrentUserLogged {

    @Autowired
    UserService userService;

    public CurrentUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nameCurrentUser = auth.getPrincipal().toString();
        Optional<UserDto> user = userService.findUser(nameCurrentUser);
        return CurrentUser.builder()
                .id(user.get().getId())
                .login(user.get().getLogin())
                .passwordHash(auth.getCredentials().toString())
                .role("ROLE_" + user.get().getRole())
                .build();
    }

}