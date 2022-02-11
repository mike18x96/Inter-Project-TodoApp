package com.inetum.training.todo.service;

import com.inetum.training.security.components.LoggedCurrentUser;
import com.inetum.training.security.model.CurrentUser;
import com.inetum.training.user.domain.dto.Todo2TodoWithoutUserDtoConverter;
import com.inetum.training.user.domain.dto.TodoDtoWithoutUser;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.persistance.TodoJpaRepository;
import com.inetum.training.user.controller.dto.UserDto;
import com.inetum.training.user.domain.User;
import com.inetum.training.user.persistance.UserRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.inetum.training.user.domain.User.Role.ADMIN;
import static com.inetum.training.user.domain.User.Role.USER;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;


public class TodoServiceTest {

    @Mock
    private LoggedCurrentUser loggedCurrentUser;

    @Mock
    private Todo2TodoWithoutUserDtoConverter todo2TodoWithoutUserDtoConverter;

    @Mock
    private UserRepository userJpaRepository;

    @Mock
    private TodoJpaRepository todoJpaRepository;

    @InjectMocks
    private TodoService todoService;


    private static final CurrentUser USER_AS_CURRENT_USER = new CurrentUser(1l, "henio", "henio", "ROLE_" + USER);
    private static final CurrentUser ADMIN_AS_CURRENT_USER = new CurrentUser(2l, "admin", "admin", "ROLE_" + ADMIN);

    private static final User USER_TEST = new User(1L, "henio", "henio", USER);
    private static final User ADMIN_TEST = new User(2L, "admin", "admin", ADMIN);
//    private static final List<User> LIST_USER = List.of(USER_TEST, ADMIN_TEST);

    private static final UserDto USER_DTO_TEST = new UserDto(1L, "henio", USER);
    private static final UserDto ADMIN_DTO_TEST = new UserDto(2L, "admin", ADMIN);
//    private static final List<UserDto> LIST_USER_DTO = List.of(USER_DTO_TEST, ADMIN_DTO_TEST);

    private static final Todo TODO_TEST_1 = new Todo(1L, "nazwa", "priorytet", "opis", true, USER_TEST);
    private static final Todo TODO_TEST_2 = new Todo(2L, "nazwa", "priorytet", "opis", true, ADMIN_TEST);
    private static final List<Todo> TODO_LIST = Arrays.asList(TODO_TEST_1, TODO_TEST_2);

    private static final TodoDtoWithoutUser TODO_DTO_1 = new TodoDtoWithoutUser(1L, "nazwa", "priorytet", "opis", true, USER_TEST.getId());
    private static final TodoDtoWithoutUser TODO_DTO_2 = new TodoDtoWithoutUser(2L, "nazwa", "priorytet", "opis", true, ADMIN_TEST.getId());
    private static final List<TodoDtoWithoutUser> TODO_DTO_LIST = Arrays.asList(TODO_DTO_1, TODO_DTO_2);

//    @ParameterizedTest
//    @MethodSource("generateValueToFindAll")
//    public void findAll_findAllTodos_returnsPageOfTodoDto(List<Todo> expectedTodos,
//                                                          List<TodoDtoWithoutUser> expectedTodosDto,
//                                                          CurrentUser currentUser) {
//        //given
//        Pageable pageable = PageRequest.of(0, 2);
//        PageImpl<Todo> todoPage = new PageImpl<>(expectedTodos, pageable, expectedTodos.size());
//
//        when(loggedCurrentUser.getCurrentUser()).thenReturn(currentUser);
//        lenient().when(todoJpaRepository.findAllByUserId(currentUser.getId(), pageable)).thenReturn(todoPage);
//        convertAllToDto(expectedTodos, expectedTodosDto);


        //when
//        Page<TodoDtoWithoutUser> todoDtoPage = todoService.findAll(pageable);
        //then
//        assertThat(todoDtoPage.getContent()).hasSize(expectedTodos.size());
//        assertThat(todoDtoPage.getContent()).containsAll(expectedDtos);
//        verify(todoJpaRepository, times(adminUsages)).findAll(FIRST_PAGE_WITH_TWO_ELEMENTS);
//        verify(todoJpaRepository, times(userUsages)).findAllByUserId(currentUser.getId(), FIRST_PAGE_WITH_TWO_ELEMENTS);
//        verifyNoMoreInteractions(todoJpaRepository);
//    }

    private static Stream<Arguments> generateValueToFindAll() {
        return Stream.of(
                of(TODO_LIST, TODO_DTO_LIST, USER_AS_CURRENT_USER));
    }

    private void convertAllToDto(List<Todo> expectedTodos, List<TodoDtoWithoutUser> expectedDtos) {
        for (int i = 0; i < expectedTodos.size(); i++) {
            when(todo2TodoWithoutUserDtoConverter.convert(expectedTodos.get(i))).thenReturn(expectedDtos.get(i));
        }
    }
}
