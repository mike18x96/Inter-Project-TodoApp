package com.inetum.training.user.controller;

import com.inetum.training.TodoAppApplication;
import com.inetum.training.user.persistance.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {TodoAppApplication.class})
@WebAppConfiguration
@SpringBootTest
public class UserRestControllerSecurityTest {

    @Autowired
    private WebApplicationContext context;

//    @Mock
//    private UserRepository repository;

    private static final String URL = "/users";

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void getAll_noCredentials_returnsUnauthrorized401() throws Exception {
        //given
        //when(repository.findAll()).thenReturn(emptyList());
        //when
        mockMvc.perform(get(URL).with(anonymous()))
        //then
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getAll_authorizedPlainUser_returnsForbidden403() throws Exception {
        //given
        //when(repository.findAll()).thenReturn(emptyList());
        //when
        mockMvc.perform(get(URL).with(user("user").roles("USER")))
        //then
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAll_authorizedAdminUser_returns200() throws Exception {
        //given
        //when(repository.findAll()).thenReturn(emptyList());
        //when
        mockMvc.perform(get(URL).with(user("admin").roles("ADMIN")))
                //then
                .andExpect(status().isOk());

    }

}
