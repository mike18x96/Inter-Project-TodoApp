package com.inetum.training.user.controller;


import com.inetum.training.user.domain.User;
import com.inetum.training.user.persistance.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class StandaloneUserRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRepository repository;

    private static final String URL = "/users";

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(new UserRestController(repository))
                .build();
    }

    @Test
    public void getAll_properRequest_returns200() throws Exception {
        //given
        MockHttpServletRequestBuilder mockRequest = get(URL);
        when(repository.findAll()).thenReturn(emptyList());
        //when
        mockMvc.perform(mockRequest)
        //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    public void getAll_UsersFound_returnsAllUsers() throws Exception {
        //given
        User user1 = User.builder().login("user1").passwordHash("pass1").role(User.Role.USER).build();
        User user2 = User.builder().login("user2").passwordHash("pass2").role(User.Role.ADMIN).build();
        when(repository.findAll()).thenReturn(newArrayList(user1, user2));
        //when
        mockMvc.perform(get(URL))
                .andDo(print())
        //then
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].login", is("user1")))
                .andExpect(jsonPath("$[1].login", is("user2")));
    }

    @Test
    public void getAll_UserFound_returnsUserDtoWithoutPassword() throws Exception {
        //given
        User user = User.builder().login("user1").passwordHash("pass1").role(User.Role.USER).build();
        when(repository.findAll()).thenReturn(newArrayList(user));
        //when
        mockMvc.perform(get(URL))
        //then
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].login", is("user1")))
                .andExpect(jsonPath("$[0].role", is("USER")))
                .andExpect(jsonPath("$[0].passwordHash").doesNotExist());
    }

}
