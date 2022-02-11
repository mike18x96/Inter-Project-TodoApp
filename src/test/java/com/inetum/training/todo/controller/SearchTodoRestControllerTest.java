package com.inetum.training.todo.controller;

import com.inetum.training.security.components.LoggedCurrentUser;
import com.inetum.training.security.model.CurrentUser;
import com.inetum.training.user.domain.dto.TodoDtoWithoutUser;
import com.inetum.training.todo.service.SearchTodoService;
import com.inetum.training.user.domain.User;
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
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class SearchTodoRestControllerTest {
    private static final String URL = "/search/todos";

    private static final User USER_TEST_ADMIN = new User(1L, "admin", "admin", ADMIN);
    private static final User USER_TEST_USER = new User(2L, "henio", "henio", USER);
    private static final CurrentUser ADMIN_CURRENT_USER = new CurrentUser(1L, "admin", "admin", "ROLE_ADMIN");
    private static final CurrentUser USER_CURRENT_USER = new CurrentUser(2L, "henio", "henio", "ROLE_USER");
    private static final TodoDtoWithoutUser TODO_DTO_1 = new TodoDtoWithoutUser(1L, "nazwa1", "priorytet1", "opis1", true, USER_TEST_USER.getId());
    private static final TodoDtoWithoutUser TODO_DTO_2 = new TodoDtoWithoutUser(2L, "nazwa2", "priorytet2", "opis2", false, USER_TEST_USER.getId());
    private static final TodoDtoWithoutUser TODO_DTO_3 = new TodoDtoWithoutUser(3L,  "nazwa3", "priorytet3", "opis3", false, USER_TEST_ADMIN.getId());
    private static final List<TodoDtoWithoutUser> TODOS_DTO_LIST_ALL = Arrays.asList(TODO_DTO_1, TODO_DTO_2, TODO_DTO_3);
    private static final List<TodoDtoWithoutUser> TODOS_DTO_LIST_ONLY_USERS = Arrays.asList(TODO_DTO_1, TODO_DTO_2);

    @Mock
    SearchTodoService searchService;

    @Mock
    private LoggedCurrentUser loggedCurrentUser;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = standaloneSetup(new SearchTodoRestController(searchService, loggedCurrentUser))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void getAll_adminRole_returnsAllTodos() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 2);
        PageImpl<TodoDtoWithoutUser> pageImp = new PageImpl<>(TODOS_DTO_LIST_ALL, pageable, TODOS_DTO_LIST_ALL.size());
        when(searchService.searchByParams(any(), any())).thenReturn(pageImp);
        when(loggedCurrentUser.getCurrentUser()).thenReturn(ADMIN_CURRENT_USER);
        //when
        mockMvc.perform(get(URL + "?page=0&size=4"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(TODOS_DTO_LIST_ALL.size())))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.content[?(@.name == 'nazwa1')].name").value("nazwa1"))
                .andExpect(jsonPath("$.content[?(@.name == 'nazwa2')].name").value("nazwa2"))
                .andExpect(jsonPath("$.content[?(@.name == 'nazwa3')].name").value("nazwa3"));
        verify(searchService, times(1)).searchByParams(any(), any());
        verify(loggedCurrentUser, times(1)).getCurrentUser();
        verifyNoMoreInteractions(searchService);
    }

    @Test
    void getAll_userRole_returnsUserTodos() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 2);
        PageImpl<TodoDtoWithoutUser> pageImp = new PageImpl<>(TODOS_DTO_LIST_ONLY_USERS, pageable, TODOS_DTO_LIST_ONLY_USERS.size());
        when(searchService.searchByParams(any(), any())).thenReturn(pageImp);
        when(loggedCurrentUser.getCurrentUser()).thenReturn(USER_CURRENT_USER);
        //when
        mockMvc.perform(get(URL + "?page=0&size=4"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(TODOS_DTO_LIST_ONLY_USERS.size())))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.content[?(@.name == 'nazwa1')].name").value("nazwa1"))
                .andExpect(jsonPath("$.content[?(@.name == 'nazwa2')].name").value("nazwa2"));
        verify(searchService, times(1)).searchByParams(any(), any());
        verify(loggedCurrentUser, times(2)).getCurrentUser();
        verifyNoMoreInteractions(searchService);
    }
}