package com.inetum.training.entity.user.service;

import com.inetum.training.entity.user.dto.UserDto;
import com.inetum.training.entity.user.dto.UserToUserDtoConverter;
import com.inetum.training.entity.user.model.User;
import com.inetum.training.entity.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public Page<UserDto> findAll(Pageable pageRequest) {
        Page<User> userPage = userRepository.findAll(pageRequest);
        final UserToUserDtoConverter userToUserDtoConverter = new UserToUserDtoConverter();
        return userPage.map(user -> userToUserDtoConverter.convert(user));
    }

    public Optional<UserDto> findUser(String userName) {

        Optional<User> currentUser = userRepository.findByLogin(userName);
        final UserToUserDtoConverter userToUserDtoConverter = new UserToUserDtoConverter();

        return Optional.ofNullable(currentUser
                .map(user -> userToUserDtoConverter.convert(user))
                .orElseThrow(() -> new EntityNotFoundException("User: " + userName + " not found!")));

    }

    public Long save(User user) {
        return userRepository.save(user).getId();
    }

    public String updatePasswordByAdmin(Long id) {
        if (userRepository.existsById(id)) {
            User user = userRepository.findById(id).get();
            String beforeEncodePassword = generatePassword(10);
            String encoderPassword = passwordEncoder.encode(beforeEncodePassword);
            user.setPasswordHash((encoderPassword));
            userRepository.save(user);
            return beforeEncodePassword;
        } else {
            throw new EntityNotFoundException();
        }
    }

    public String updatePasswordByUser(String loginUser) {

        User user = userRepository.findByLogin(loginUser).get();

        String beforeEncodePassword = generatePassword(10);
        String encoderPassword = passwordEncoder.encode(beforeEncodePassword);
        user.setPasswordHash((encoderPassword));
        userRepository.save(user);
        return "password: " + beforeEncodePassword;

    }

    public String updateUserRole(Long id, String role) {
        if (userRepository.existsById(id)) {
            User user = userRepository.findById(id).get();
            user.setRole(User.Role.valueOf(role.toUpperCase(Locale.ROOT)));
            userRepository.save(user);
            return role.toUpperCase(Locale.ROOT);
        } else {
            throw new EntityNotFoundException();
        }
    }

    public String deleteByIdForAdmin(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return "User deleted";
        } else {
            throw new EntityNotFoundException();
        }
    }

    public String generatePassword(int length) {
        return new Random().ints(length, 33, 122).collect(StringBuilder::new,
                        StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String notFoundEntityHandler() {
        return "Not found object with this id";
    }

}
