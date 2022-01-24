package com.inetum.training.user.controller;

import com.inetum.training.user.controller.dto.UserWithoutPasswordDto;
import com.inetum.training.user.persistance.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserRepository userRepository;

    @GetMapping
    public List<UserWithoutPasswordDto> getAll(){
        return userRepository.findAll().stream()
                .map(UserWithoutPasswordDto::new)
                .collect(Collectors.toList());
    }

}
