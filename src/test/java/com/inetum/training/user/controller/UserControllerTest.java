//package com.inetum.training.user.controller;
//
//import com.inetum.training.security.model.CurrentUser;
//import com.inetum.training.user.controller.dto.UserDto;
//import com.inetum.training.user.domain.User;
//import com.inetum.training.user.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.web.servlet.MockMvc;
//
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
//@Disabled
//public class UserControllerTest {
//
//    @Mock
//    private UserService userService;
//
//    private MockMvc mockMvc;
//    public static final String URL = "/users";
//
//    private static final CurrentUser currentUserUSER = new CurrentUser(1l, "henio", "henio", "ROLE_" + USER);
//
//    private static final User USER_TEST = new User(1L, "henio", "henio", USER);
//    private static final User ADMIN_TEST = new User(2L, "admin", "admin", ADMIN);
//    private static final User NEW_USER_TEST = new User(1L, "henio", "password", USER);
//    private static final List<User> listUser = List.of(USER_TEST, ADMIN_TEST);
//
//    private static final UserDto USER_DTO_TEST = new UserDto(1L, "henio", USER);
//    private static final UserDto ADMIN_DTO_TEST = new UserDto(2L, "admin", ADMIN);
//    private static final List<UserDto> LIST_USER_DTO = List.of(USER_DTO_TEST, ADMIN_DTO_TEST);
//
//
//    @BeforeEach
//    public void setUp() {
//        mockMvc = standaloneSetup(new UserRestController(userService))
//                .build();
//    }
//
//    @Test
//    public void getAll_UserFound_returnsUserWithoutPassword() throws Exception {
//
//        //given
//        Pageable pageable = PageRequest.of(0, 2);
//        PageImpl<UserDto> pageImp = new PageImpl<>(LIST_USER_DTO, pageable, LIST_USER_DTO.size());
//        when(userService.findAll(pageable)).thenReturn(pageImp);
//
//        //when
//        mockMvc.perform(get(URL)
//                        .param("page", String.valueOf(pageable.getPageNumber()))
//                        .param("size", String.valueOf(pageable.getPageSize()))
//                        .contentType(APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(status().isOk());
////        verify(todoService, times(1)).findAll(pageable);
//    }
//
//}
