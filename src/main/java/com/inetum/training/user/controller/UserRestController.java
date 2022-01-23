package com.inetum.training.user.controller;

import com.google.common.collect.Lists;
import com.inetum.training.user.controller.dto.UserDtoWithoutPassword;
import com.inetum.training.user.persistance.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserJpaRepository userJpaRepository;

    @GetMapping
    public List<UserDtoWithoutPassword> getAllWithoutPassword() {
        Iterable<UserDtoWithoutPassword> users = userJpaRepository.getUserWithoutPassword();
        return Lists.newArrayList(users);
    }

}
