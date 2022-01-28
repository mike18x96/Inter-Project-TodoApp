package com.inetum.training.user.controller;


import com.inetum.training.user.controller.dto.UserDto;
import com.inetum.training.user.domain.User;
import com.inetum.training.user.service.SearchUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static com.inetum.training.user.domain.User.Role.ADMIN;
import static com.inetum.training.user.domain.User.Role.USER;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@ExtendWith(MockitoExtension.class)
public class StandaloneUserRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SearchUserService userService;

    private static final String URL = "/users";
    private static final User user_1 = new User("user1", "password1", USER);
    private static final User user_2 = new User("user2", "password2", ADMIN);
    private static final User user_3 = new User("user3", "password3", ADMIN);
    private static final List<User> listUser = Arrays.asList(user_1, user_2, user_3);

    private static final UserDto userDto_1 = new UserDto(user_1);
    private static final UserDto userDto_2 = new UserDto(user_2);
    private static final UserDto userDto_3 = new UserDto(user_3);
    private static final List<UserDto> listUserDto = Arrays.asList(userDto_1, userDto_2, userDto_3);

    @BeforeEach
    public void setUp(){
        mockMvc = standaloneSetup(new UserRestController(userService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    public void getAll_UsersFound_returnsAllUsers() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 2);
        PageImpl<UserDto> pageImp = new PageImpl<>(listUserDto, pageable, listUser.size());
        when(userService.findAllWithoutPassword(any(Pageable.class))).thenReturn(pageImp);
        //when
        mockMvc.perform(get(URL))
                .andDo(print())
        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(2)).andReturn();
    }

//    @Test
//    public void getAll_UserFound_returnsUserDtoWithoutPassword() throws Exception {
//        //given
//        User user = User.builder().login("user1").passwordHash("pass1").role(User.Role.USER).build();
//        when(userService.findAll()).thenReturn(newArrayList(user));
//        //when
//        mockMvc.perform(get(URL))
//        //then
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].login", is("user1")))
//                .andExpect(jsonPath("$[0].role", is("USER")))
//                .andExpect(jsonPath("$[0].passwordHash").doesNotExist());
//    }

}
