package com.inetum.training.user.service;

import com.inetum.training.user.controller.dto.UserDto;
import com.inetum.training.user.domain.User;
import com.inetum.training.user.persistance.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class SearchUserService {

    private final UserRepository userRepository;

    public Page<UserDto> findAllWithoutPassword(Pageable pageable) {
        Page<User> pageOfUser = userRepository.findAll(pageable);
        return pageOfUser.map(user -> new UserDto(user));
    }

}
