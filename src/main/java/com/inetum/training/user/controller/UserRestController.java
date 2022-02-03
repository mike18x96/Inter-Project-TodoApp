package com.inetum.training.user.controller;

import com.inetum.training.user.controller.dto.UserDto;
import com.inetum.training.user.domain.User;
import com.inetum.training.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserRestController {

    private final UserService userService;

    @GetMapping
    public Page<UserDto> getAllWithoutPassword(Pageable pageRequest) {
        return userService.findAll(pageRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid User user) {
        return userService.save(user);
    }

    @PutMapping("resetPassword/{id}")
    public String updatePasswordByAdmin(@PathVariable("id") Long id) {
        return userService.updatePasswordByAdmin(id);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("resetUserPasswordByUser/{loginUser}/{oldPassword}")
    public String updatePasswordByUser(@PathVariable("loginUser") String loginUser, @PathVariable("oldPassword") String oldPassword) {
        return userService.updatePasswordByUser(loginUser, oldPassword);
    }

    @PutMapping("updateRoleByAdmin/{id}/{role}")
    public String updateUserRole(@PathVariable("id") Long id, @PathVariable("role") String role) {
        return userService.updateUserPermissions(id, role);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        userService.deleteByIdForAdmin(id);
    }

}
