package com.inetum.training.user.controller.dto;

import com.inetum.training.user.domain.User;
import org.springframework.core.convert.converter.Converter;

public class UserToUserDtoConverter implements Converter<User, UserDto> {
    @Override
    public UserDto convert(User user) {

        return new UserDto(user.getId(), user.getLogin(), user.getRole());
    }
}
