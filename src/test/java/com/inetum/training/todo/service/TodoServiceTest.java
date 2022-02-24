//package com.inetum.training.todo.service;
//
//import com.inetum.training.entity.todo.service.TodoService;
//import com.inetum.training.security.LoggedCurrentUser;
//import com.inetum.training.security.CurrentUser;
//import com.inetum.training.entity.todo.model.Todo;
//import com.inetum.training.entity.todo.dto.Todo2TodoWithoutUserDtoConverter;
//import com.inetum.training.entity.todo.dto.TodoDtoWithoutUser;
//import com.inetum.training.entity.todo.repository.TodoJpaRepository;
//import com.inetum.training.entity.user.model.User;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Stream;
//
//import static com.inetum.training.entity.user.model.User.Role.ADMIN;
//import static com.inetum.training.entity.user.model.User.Role.USER;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.params.provider.Arguments.of;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class TodoServiceTest {
//
//    @Mock
//    private LoggedCurrentUser loggedCurrentUser;
//
//    @Mock
//    private Todo2TodoWithoutUserDtoConverter todo2TodoWithoutUserDtoConverter;
//
//    @Mock
//    private TodoJpaRepository todoJpaRepository;
//
//    @InjectMocks
//    private TodoService todoService;
//
//
//    private static final CurrentUser USER_AS_CURRENT_USER = new CurrentUser(1l, "henio", "henio", "ROLE_USER");
//    private static final CurrentUser ADMIN_AS_CURRENT_USER = new CurrentUser(2l, "admin", "admin", "ROLE_ADMIN");
//
//    private static final User USER_TEST = new User(1L, "henio", "henio", USER);
//    private static final User ADMIN_TEST = new User(2L, "admin", "admin", ADMIN);
//
//    private static final Todo TODO_TEST_1 = new Todo(1L, "nazwa", "priorytet", "opis", true, USER_TEST);
//    private static final Todo TODO_TEST_2 = new Todo(2L, "nazwa", "priorytet", "opis", true, ADMIN_TEST);
//    private static final List<Todo> TODO_LIST = Arrays.asList(TODO_TEST_1, TODO_TEST_2);
//
//    private static final TodoDtoWithoutUser TODO_DTO_1 = new TodoDtoWithoutUser(1L, "nazwa", "priorytet", "opis", true, 1L);
//    private static final TodoDtoWithoutUser TODO_DTO_2 = new TodoDtoWithoutUser(2L, "nazwa", "priorytet", "opis", true, 2L);
//    private static final List<TodoDtoWithoutUser> TODO_DTO_LIST = Arrays.asList(TODO_DTO_1, TODO_DTO_2);
//
//    @ParameterizedTest
//    @MethodSource("generateValueToFindAllByUser")
//    public void findAll_findAllTodosByUser_returnsPageOfTodoDto(List<Todo> expectedTodos,
//                                                                List<TodoDtoWithoutUser> expectedTodosDto,
//                                                                CurrentUser currentUser) {
//        //given
//        Pageable pageable = PageRequest.of(0, 2);
//        PageImpl<Todo> todoPage = new PageImpl<>(expectedTodos, pageable, expectedTodos.size());
//
//        when(loggedCurrentUser.getCurrentUser()).thenReturn(currentUser);
//        when(todoJpaRepository.findAllByUserId(currentUser.getId(), pageable)).thenReturn(todoPage);
//        convertAllToDto(expectedTodos, expectedTodosDto);
//        //when
//        Page<TodoDtoWithoutUser> todoDtoPage = todoService.findAll(pageable);
//        //then
//        assertThat(todoDtoPage.getContent()).hasSize(expectedTodos.size());
//        assertThat(todoDtoPage.getContent()).containsAll(expectedTodosDto);
//        verify(loggedCurrentUser, times(2)).getCurrentUser();
//        verify(todoJpaRepository, times(1)).findAllByUserId(currentUser.getId(), pageable);
//        verifyNoMoreInteractions(todoJpaRepository);
//    }
//
//    private static Stream<Arguments> generateValueToFindAllByUser() {
//        return Stream.of(
//                of(TODO_LIST, TODO_DTO_LIST, USER_AS_CURRENT_USER));
//    }
//
//    @ParameterizedTest
//    @MethodSource("generateValueToFindAllByAdmin")
//    public void findAll_findAllTodosByAdmin_returnsPageOfTodoDto(List<Todo> expectedTodos,
//                                                                 List<TodoDtoWithoutUser> expectedTodosDto,
//                                                                 CurrentUser currentUser) {
//        //given
//        Pageable pageable = PageRequest.of(0, 2);
//        PageImpl<Todo> todoPage = new PageImpl<>(expectedTodos, pageable, expectedTodos.size());
//
//        when(loggedCurrentUser.getCurrentUser()).thenReturn(currentUser);
//        when(todoJpaRepository.findAll(pageable)).thenReturn(todoPage);
//        convertAllToDto(expectedTodos, expectedTodosDto);
//        //when
//        Page<TodoDtoWithoutUser> todoDtoPage = todoService.findAll(pageable);
//        //then
//        assertThat(todoDtoPage.getContent()).hasSize(expectedTodos.size());
//        assertThat(todoDtoPage.getContent()).containsAll(expectedTodosDto);
//        verify(loggedCurrentUser, times(1)).getCurrentUser();
//        verify(todoJpaRepository, times(1)).findAll(pageable);
//        verifyNoMoreInteractions(todoJpaRepository);
//    }
//
//    private static Stream<Arguments> generateValueToFindAllByAdmin() {
//        return Stream.of(
//                of(TODO_LIST, TODO_DTO_LIST, ADMIN_AS_CURRENT_USER));
//    }
//
//    @ParameterizedTest
//    @MethodSource("generateValueByUserToFindById")
//    public void findById_findAllTodosByAdmin_returndoDto(Todo expectedTodo,
//                                                         TodoDtoWithoutUser expectedTodoDto,
//                                                         CurrentUser currentUser) {
//        //given
//        when(todoJpaRepository.existsById(1L)).thenReturn(true);
//        when(loggedCurrentUser.getCurrentUser()).thenReturn(currentUser);
//        when(todoJpaRepository.findById(1L)).thenReturn(Optional.ofNullable(expectedTodo));
//        //when
//        TodoDtoWithoutUser todoDtoWithoutUser = todoService.findById(1L);
//        //then
//        assertThat(todoDtoWithoutUser).isEqualTo(expectedTodoDto);
//        verify(todoJpaRepository, times(1)).existsById(1l);
//        verify(loggedCurrentUser, times(2)).getCurrentUser();
//        verify(todoJpaRepository, times(2)).findById(1l);
//        verifyNoMoreInteractions(todoJpaRepository);
//    }
//
//    private static Stream<Arguments> generateValueByAdminToFindById() {
//        return Stream.of(
//                of(TODO_TEST_1, TODO_DTO_1, ADMIN_AS_CURRENT_USER));
//    }
//
//    @ParameterizedTest
//    @MethodSource("generateValueByUserToFindById")
//    public void findById_findAllTodosByUser_returnTodoDto(Todo expectedTodo,
//                                                          TodoDtoWithoutUser expectedTodoDto,
//                                                          CurrentUser currentUser) {
//        //given
//        when(todoJpaRepository.existsById(1L)).thenReturn(true);
//        when(loggedCurrentUser.getCurrentUser()).thenReturn(currentUser);
//        when(todoJpaRepository.findById(1L)).thenReturn(Optional.ofNullable(expectedTodo));
//        //when
//        TodoDtoWithoutUser todoDtoWithoutUser = todoService.findById(1L);
//        //then
//        assertThat(todoDtoWithoutUser).isEqualTo(expectedTodoDto);
//        verify(todoJpaRepository, times(1)).existsById(1l);
//        verify(loggedCurrentUser, times(2)).getCurrentUser();
//        verify(todoJpaRepository, times(2)).findById(1l);
//        verifyNoMoreInteractions(todoJpaRepository);
//    }
//
//    private static Stream<Arguments> generateValueByUserToFindById() {
//        return Stream.of(
//                of(TODO_TEST_1, TODO_DTO_1, USER_AS_CURRENT_USER));
//    }
//
//    @Test
//    public void existsById_correctFound_returnTodoId() {
//        //given
//        when(todoJpaRepository.existsById(1l)).thenReturn(true);
//        //when
//        boolean existsByid = todoService.existsById(1l);
//        //then
//        verify(todoJpaRepository, times(1)).existsById(anyLong());
//        verifyNoMoreInteractions(todoJpaRepository);
//    }
//
//    @Test
//    public void save_saveCorrect_returnTodoId() {
//        //given
//        when(loggedCurrentUser.getCurrentUser()).thenReturn(USER_AS_CURRENT_USER);
//        when(todoJpaRepository.save(TODO_TEST_1)).thenReturn(TODO_TEST_1);
//        //when
//        Long todoId = todoService.save(TODO_TEST_1);
//        //then
//        assertThat(todoId).isEqualTo(1L);
//        verify(loggedCurrentUser, times(1)).getCurrentUser();
//        verify(todoJpaRepository, times(1)).save(any());
//        verifyNoMoreInteractions(todoJpaRepository);
//    }
//
//    @Test
//    public void deleteById_correctDeleteByAdmin_deletedTodo() {
//        //given
//        when(todoJpaRepository.existsById(1l)).thenReturn(true);
//        when(loggedCurrentUser.getCurrentUser()).thenReturn(ADMIN_AS_CURRENT_USER);
//        //when
//        todoService.deleteById(1l);
//        //then
//        verify(todoJpaRepository, times(1)).existsById(anyLong());
//        verify(todoJpaRepository, times(1)).deleteById(anyLong());
//        verify(loggedCurrentUser, times(1)).getCurrentUser();
//        verifyNoMoreInteractions(todoJpaRepository);
//
//    }
//
//    private void convertAllToDto(List<Todo> expectedTodos, List<TodoDtoWithoutUser> expectedTodoDto) {
//        for (int i = 0; i < expectedTodos.size(); i++) {
//            lenient().when(todo2TodoWithoutUserDtoConverter.convert(expectedTodos.get(i))).thenReturn(expectedTodoDto.get(i));
//        }
//    }
//}
