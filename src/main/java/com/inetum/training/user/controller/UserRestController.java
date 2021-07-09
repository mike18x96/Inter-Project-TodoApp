package com.inetum.training.user.controller;

import com.inetum.training.user.controller.dto.User2UserDtoConverter;
import com.inetum.training.user.controller.dto.UserDto;
import com.inetum.training.user.domain.User;
import com.inetum.training.user.persistance.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public Page<UserDto> getAll(Pageable pageRequest){
        Page<User> userPage = userRepository.findAll(pageRequest);
        final User2UserDtoConverter user2UserDtoConverter = new User2UserDtoConverter();
        return userPage.map(user2UserDtoConverter::convert);
    }
}
