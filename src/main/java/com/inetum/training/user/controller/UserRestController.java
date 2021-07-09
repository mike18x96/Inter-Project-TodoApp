package com.inetum.training.user.controller;

import com.inetum.training.user.controller.dto.UserDto;
import com.inetum.training.user.controller.dto.UserToUserDtoConverter;
import com.inetum.training.user.domain.User;
import com.inetum.training.user.persistance.UserRepository;
import com.inetum.training.user.service.SearchUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserRestController {

    private final UserRepository userRepository;

    @GetMapping
    public Page<UserDto> getAllWithoutPassword(Pageable pageRequest) {
        Page<User> userPage = userRepository.findAll(pageRequest);
        final UserToUserDtoConverter user2UserDtoConverter = new UserToUserDtoConverter();
        return userPage.map(user2UserDtoConverter::convert);
    }

}
