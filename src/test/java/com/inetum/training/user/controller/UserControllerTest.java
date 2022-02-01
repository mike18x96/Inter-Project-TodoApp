//package com.inetum.training.user.controller;
//
//import com.inetum.training.user.domain.User;
//import com.inetum.training.user.persistance.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static com.inetum.training.user.domain.User.Role.ADMIN;
//import static com.inetum.training.user.domain.User.Role.USER;
//import static org.hamcrest.Matchers.hasSize;
//import static org.mockito.Mockito.*;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
//
//@ExtendWith(MockitoExtension.class)
//public class UserControllerTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    private MockMvc mockMvc;
//    public static final String URL = "/users";
//
//    @BeforeEach
//    public void setUp() {
//        mockMvc = standaloneSetup(new UserRestController(userRepository))
//                .build();
//    }
//
//    private static final User user1 = new User("login1", "password1", USER);
//    private static final User user2 = new User("login2", "password2", ADMIN);
//
//    List<User> listUser = Arrays.asList(user1, user2);
//
//    @Test
//    public void getAll_UserFound_returnsUserWithoutPassword() throws Exception {
//        //given
//        when(userRepository.findAll()).thenReturn(listUser);
//        //when
//        mockMvc.perform(get(URL)
//                        .contentType(APPLICATION_JSON)
//                        .accept(APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[?(@.login == 'login1')].login").value("login1"))
//                .andExpect(jsonPath("$[?(@.login == 'login2')].login").value("login2"))
//                .andExpect(jsonPath("$[?(@.role == 'USER')].role").value("USER"))
//                .andExpect(jsonPath("$[?(@.role == 'ADMIN')].role").value("ADMIN"))
//                .andExpect(jsonPath("$[0].passwordHash").doesNotExist())
//                .andExpect(jsonPath("$[1].passwordHash").doesNotExist());
//        verify(userRepository, times(1)).findAll();
//
//    }
//
//}
