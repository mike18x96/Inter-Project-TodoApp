package com.inetum.training.user.service;

import com.inetum.training.security.model.CurrentUser;
import com.inetum.training.user.controller.dto.UserDto;
import com.inetum.training.user.controller.dto.UserToUserDtoConverter;
import com.inetum.training.user.domain.User;
import com.inetum.training.user.persistance.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import java.util.Locale;
import java.util.Random;

@Service
//@RequiredArgsConstructor
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    public void deleteByIdForAdmin(Long id) {
        if (userRepository.existsById(id)) {
            if ((getCurrentUser().getRole()).equals("ROLE_ADMIN")) {
                userRepository.deleteById(id);
            } else {
                System.out.println("You are not authorized");
            }
        } else {
            throw new EntityNotFoundException();
        }
    }

    public Page<UserDto> findAll(Pageable pageRequest) {
        Page<User> userPage = userRepository.findAll(pageRequest);
        final UserToUserDtoConverter user2UserDtoConverter = new UserToUserDtoConverter();
        return userPage.map(user -> user2UserDtoConverter.convert(user));
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

    public String updatePasswordByUser(String loginUser, String oldPassword) {

        if ((getCurrentUser().getRole()).equals("ROLE_ADMIN")) {
            return "wrong URL to change Admin password";
        }

        if (getCurrentUser().getLogin().equals(loginUser)) {
            User user = userRepository.findByLogin(loginUser).get();

            if (passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
                String beforeEncodePassword = generatePassword(10);
                String encoderPassword = passwordEncoder.encode(beforeEncodePassword);
                user.setPasswordHash((encoderPassword));
                userRepository.save(user);
                return "password: " + beforeEncodePassword;
            } else {
                return "Wrong password";
            }
        } else {
            throw new EntityNotFoundException();
        }
    }

    public String updateUserPermissions(Long id, String role) {
        if (userRepository.existsById(id)) {
            User user = userRepository.findById(id).get();
            user.setRole(User.Role.valueOf(role.toUpperCase(Locale.ROOT)));
            userRepository.save(user);
            return role.toUpperCase(Locale.ROOT);
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

    CurrentUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (CurrentUser) auth.getPrincipal();
    }


}
