package com.inetum.training.user.service;

import com.inetum.training.security.model.CurrentUser;
import com.inetum.training.user.controller.dto.UserDto;
import com.inetum.training.user.controller.dto.UserToUserDtoConverter;
import com.inetum.training.user.domain.User;
import com.inetum.training.user.persistance.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static com.inetum.training.user.domain.User.Role.ADMIN;
import static com.inetum.training.user.domain.User.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserToUserDtoConverter userToUserDtoConverter;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private static final CurrentUser currentUserUSER = new CurrentUser(1l, "henio", "henio", "ROLE_" + USER);

    private static final User USER_TEST = new User(1L, "henio", "henio", USER);
    private static final User ADMIN_TEST = new User(2L, "admin", "admin", ADMIN);
    private static final User NEW_USER_TEST = new User(1L, "henio", "password", USER);
    private static final List<User> LIST_USER = List.of(USER_TEST, ADMIN_TEST);

    private static final UserDto USER_DTO_TEST = new UserDto(1L, "henio", USER);
    private static final UserDto ADMIN_DTO_TEST = new UserDto(2L, "admin", ADMIN);
    private static final List<UserDto> LIST_USER_DTO = List.of(USER_DTO_TEST, ADMIN_DTO_TEST);

    @Test
    void findAll_byPageRequest_returnPageOfUserDto() {

        //given
        Pageable pageable = PageRequest.of(0, 2);
        PageImpl<User> pageImp = new PageImpl<>(LIST_USER, pageable, LIST_USER.size());
        when(userRepository.findAll(pageable)).thenReturn(pageImp);
        lenient().when(userToUserDtoConverter.convert(USER_TEST)).thenReturn(USER_DTO_TEST);
        lenient().when(userToUserDtoConverter.convert(ADMIN_TEST)).thenReturn(ADMIN_DTO_TEST);
        //when
        Page<UserDto> userDtoPage = userService.findAll(pageable);
        //then
        assertThat(userDtoPage.getContent()).hasSize(LIST_USER_DTO.size());
        verify(userRepository, times(1)).findAll(pageable);

    }

    @Test
    void save() {
        //given
        when(userRepository.save(NEW_USER_TEST)).thenReturn(NEW_USER_TEST);
        //when
        Long saveUser = userService.save(NEW_USER_TEST);
        //then
        assertThat(saveUser).isEqualTo(NEW_USER_TEST.getId());
        verify(userRepository, times(1)).save(NEW_USER_TEST);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updatePasswordByAdmin_correctReset_returnNewPassword() {
        //given
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(USER_TEST));
        //when
        String passwordUser = userService.updatePasswordByAdmin(1L);
        //then
        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).findById(1l);
        verify(userRepository, times(1)).existsById(1l);
        verify(passwordEncoder, times(1)).encode(anyString());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updatePasswordByAdmin_notExistId_returnEntityNotFoundException() {
        //given
        when(userRepository.existsById(anyLong())).thenReturn(false);
        //when
        assertThatThrownBy(() -> userService.updatePasswordByAdmin(anyLong()))
                .isInstanceOf(EntityNotFoundException.class);
        //then
        verify(userRepository, times(1)).existsById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updatePasswordByUser_correctReset_returnNewPassword() {
        //given
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(USER_TEST));
        when(passwordEncoder.encode(any())).thenReturn(any());
        when(userRepository.save(NEW_USER_TEST)).thenReturn(NEW_USER_TEST);

        //when
        String passwordUser = userService.updatePasswordByUser(currentUserUSER.getLogin());
        //then
        verify(userRepository, times(1)).findByLogin(anyString());
        verify(passwordEncoder, times(1)).encode(any());
        verify(userRepository, times(1)).save(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateUserPermissions_correctUpdate_returnNewRole() {
        //given
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(USER_TEST));
        when(userRepository.save(USER_TEST)).thenReturn(USER_TEST);
        //when
        String newRole = userService.updateUserRole(USER_TEST.getId(), "ADMIN");
        //then
        verify(userRepository, times(1)).existsById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateUserPermissions_incorrectUpdate_returnNewRole() {
        //given
        when(userRepository.existsById(anyLong())).thenReturn(false);
        //when
        assertThatThrownBy(() -> userService.updateUserRole(USER_TEST.getId(), "ADMIN"))
                .isInstanceOf(EntityNotFoundException.class);
        //then
        verify(userRepository, times(1)).existsById(anyLong());
        verify(userRepository, never()).findById(anyLong());
        verify(userRepository, never()).save(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteByIdForAdmin_authorizedAdmin_deleteElementFromRepo() {
        //given
        when(userRepository.existsById(anyLong())).thenReturn(true);
        //when
        userService.deleteByIdForAdmin(anyLong());
        //then
        verify(userRepository, times(1)).existsById(anyLong());
        verify(userRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(userRepository);
    }

}