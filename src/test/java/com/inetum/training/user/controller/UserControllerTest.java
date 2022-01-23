package com.inetum.training.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.user.controller.dto.UserDtoWithoutPassword;
import com.inetum.training.user.domain.User;
import com.inetum.training.user.persistance.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.inetum.training.user.domain.User.Role.ADMIN;
import static com.inetum.training.user.domain.User.Role.USER;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    private MockMvc mockMvc;
    public static final String URL = "/user";

    @BeforeEach
    public void setUp() {
        mockMvc = standaloneSetup(new UserRestController(userJpaRepository))
                .build();
    }

    private static final User user1 = User.builder()
            .login("login1")
            .passwordHash("password1")
            .role(USER)
            .build();
    private static final User user2 = User.builder()
            .login("login2")
            .passwordHash("password2")
            .role(ADMIN)
            .build();
    private static final UserDtoWithoutPassword userDtoWithoutPassword1 = new UserDtoWithoutPassword(user1);
    private static final UserDtoWithoutPassword userDtoWithoutPassword2 = new UserDtoWithoutPassword(user2);
    List<UserDtoWithoutPassword> listUser = Arrays.asList(userDtoWithoutPassword1, userDtoWithoutPassword2);

    @Test
    public void getAll_UserFound_returnsUserWithoutPassword() throws Exception {
        //given
        when(userJpaRepository.getUserWithoutPassword()).thenReturn(listUser);
        //when
        mockMvc.perform(get(URL)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].login").value("login1"))
                .andExpect(jsonPath("$[1].login").value("login2"))
                .andExpect(jsonPath("$[0].role").value("USER"))
                .andExpect(jsonPath("$[1].role").value("ADMIN"));
        verify(userJpaRepository, times(1)).getUserWithoutPassword();

    }


}
