//package com.inetum.training.user.controller;
//
//import com.inetum.training.entity.user.controller.UserController;
//import com.inetum.training.entity.user.dto.UserDto;
//import com.inetum.training.entity.user.model.User;
//import com.inetum.training.entity.user.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
//import org.springframework.test.web.servlet.MockMvc;
//
//import javax.persistence.EntityNotFoundException;
//import java.util.List;
//
//import static com.inetum.training.TestJsonUtils.convertObjectToJson;
//import static com.inetum.training.entity.user.model.User.Role.ADMIN;
//import static com.inetum.training.entity.user.model.User.Role.USER;
//import static org.hamcrest.Matchers.is;
//import static org.mockito.Mockito.*;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
//    private static final User USER_TEST = new User(1L, "henio", "henio", USER);
//
//    private static final UserDto USER_DTO_TEST = new UserDto(1L, "henio", USER);
//    private static final UserDto ADMIN_DTO_TEST = new UserDto(2L, "admin", ADMIN);
//    private static final List<UserDto> LIST_USER_DTO = List.of(USER_DTO_TEST, ADMIN_DTO_TEST);
//
//    @BeforeEach
//    public void setUp() {
//        mockMvc = standaloneSetup(new UserController(userService))
//                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
//                .build();
//    }
//
//    @Test
//    public void getAllWithoutPassword_UserFound_returnsUserWithoutPassword() throws Exception {
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
//        verify(userService, times(1)).findAll(pageable);
//    }
//
//    @Test
//    public void create_elementWithoutLogin_returns400() throws Exception {
//        //given
//        User user = User.builder()
//                .passwordHash("aa")
//                .role(USER)
//                .build();
//        //when
//        mockMvc.perform(post(URL)
//                        .contentType(APPLICATION_JSON)
//                        .content(convertObjectToJson(user))
//                        .accept(APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(status().is4xxClientError());
//    }
//
//    @Test
//    public void create_completeElement_returns201() throws Exception {
//        //given
//        User user = User.builder()
//                .id(1L)
//                .login("login")
//                .passwordHash("password")
//                .role(USER)
//                .build();
//        when(userService.save(user)).thenReturn(1L);
//        //when
//        mockMvc.perform(post(URL)
//                        .contentType(APPLICATION_JSON)
//                        .content(convertObjectToJson(user)))
//                .andDo(print())
//                //then
//                .andExpect(jsonPath("$", is(1)))
//                .andExpect(status().isCreated());
//        verify(userService, times(1)).save(user);
//    }
//
//    @Test
//    public void updatePasswordByAdmin_idNotExist_returns404() throws Exception {
//        //given
//        when(userService.updatePasswordByAdmin(1L)).thenThrow(new EntityNotFoundException());
//        //when
//        mockMvc.perform(put(URL + "/resetPasswordByAdmin/" + 1L)
//                        .contentType(APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(jsonPath("$", is(String.format("Not found element with id: %s", 1L))))
//                .andExpect(status().isNotFound());
//        verify(userService, times(1)).updatePasswordByAdmin(1L);
//        verifyNoMoreInteractions(userService);
//    }
//
//    @Test
//    public void updatePasswordByAdmin_idExist_returns200() throws Exception {
//        //given
//        when(userService.updatePasswordByAdmin(1L)).thenReturn("generatedPassword");
//        //when
//        mockMvc.perform(put(URL + "/resetPasswordByAdmin/" + 1L)
//                        .contentType(APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", is(String.format("generatedPassword"))));
//        verify(userService, times(1)).updatePasswordByAdmin(1L);
//        verifyNoMoreInteractions(userService);
//    }
//
//    @Test
//    public void updatePasswordByUser_correctLoginAndPassword_returns200() throws Exception {
//        //given
//        when(userService.updatePasswordByUser(USER_TEST.getLogin())).thenReturn("generatedPassword");
//        //when
//        mockMvc.perform(put(URL + "/resetUserPasswordByUser/" + USER_TEST.getLogin() + "/" + USER_TEST.getPasswordHash())
//                        .contentType(APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", is(String.format("generatedPassword"))));
//        verify(userService, times(1)).updatePasswordByUser(USER_TEST.getLogin());
//        verifyNoMoreInteractions(userService);
//    }
//
//    @Test
//    public void updateUserRoleByAdmin_correctId_returns200() throws Exception {
//        when(userService.updateUserRole(USER_TEST.getId(), "ADMIN")).thenReturn("ADMIN");
//        //when
//        mockMvc.perform(put(URL + "/updateRoleByAdmin" + "/" + USER_TEST.getId() + "/ADMIN")
//                        .contentType(APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", is(String.format("ADMIN"))));
//        verify(userService, times(1)).updateUserRole(anyLong(), anyString());
//        verifyNoMoreInteractions(userService);
//    }
//
//    @Test
//    public void updateUserRoleByAdmin_incorrectId_returns404() throws Exception {
//        when(userService.updateUserRole(USER_TEST.getId(), "ADMIN")).thenThrow(new EntityNotFoundException(USER_TEST.getId().toString()));
//        //when
//        mockMvc.perform(put(URL + "/updateRoleByAdmin" + "/" + USER_TEST.getId() + "/ADMIN")
//                        .contentType(APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(jsonPath("$", is(String.format("Not found element with id: %s", USER_TEST.getId()))))
//                .andExpect(status().isNotFound());
//        verify(userService, times(1)).updateUserRole(anyLong(), anyString());
//        verifyNoMoreInteractions(userService);
//    }
//
//    @Test
//    public void delete_correctId_returns204() throws Exception {
//        lenient().when(userService.deleteByIdForAdmin(1l)).thenReturn("User deleted");
//        //when
//        mockMvc.perform(delete(URL + "/1")
//                        .contentType(APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(status().isNoContent());
//        verify(userService, times(1)).deleteByIdForAdmin(anyLong());
//        verifyNoMoreInteractions(userService);
//    }
//
//    @Test
//    public void delete_incorrectId_returns404() throws Exception {
//        lenient().when(userService.deleteByIdForAdmin(1l)).thenThrow(new EntityNotFoundException("1"));
//        //when
//        mockMvc.perform(delete(URL + "/1")
//                        .contentType(APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$", is(String.format("Not found element with id: %s", 1L))));
//        verify(userService, times(1)).deleteByIdForAdmin(anyLong());
//        verifyNoMoreInteractions(userService);
//    }
//}
