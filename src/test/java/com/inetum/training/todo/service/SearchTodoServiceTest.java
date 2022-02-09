package com.inetum.training.todo.service;

import com.inetum.training.security.model.CurrentUser;
import com.inetum.training.todo.controller.dto.TodoSearchParamsDto;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.persistance.TodoJpaRepository;
import com.inetum.training.user.domain.User;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.inetum.training.user.domain.User.Role.ADMIN;
import static com.inetum.training.user.domain.User.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchTodoServiceTest {

    @Mock
    private TodoJpaRepository todoJpaRepository;

    @Mock
    private TodoService todoService;

    @InjectMocks
    private SearchTodoService searchTodoService;

    private static final User USER_TEST = new User(1L, "henio", "henio", USER);
    private static final User ADMIN_TEST = new User(2L, "admin", "admin", ADMIN);

    private static final Todo todo_1 = new Todo(1L, "nazwa", "priorytet", "opis1", true, USER_TEST);
    private static final Todo todo_2 = new Todo(2L, "nazwa", "priorytet", "opis2", true, USER_TEST);
    private static final List<Todo> listTodos = Arrays.asList(todo_1, todo_2);
    private static final List<Todo> listTodosForUser = Arrays.asList(todo_1);

    private static final CurrentUser currentUserUSER = new CurrentUser(1l, "henio", "henio", "ROLE_" + USER);
    private static final CurrentUser currentUserADMIN = new CurrentUser(2l, "admin", "admin", "ROLE_" + ADMIN);

    @ParameterizedTest
    @MethodSource("generateValueByOnlyNameForAdmin")
    public void findByAdmin_searchByNameOnly_returnsElementFoundInRepo(List<Todo> expectedTodoForAdmin, String generateName) {
        //given
        Pageable pageable = PageRequest.of(0, 1);
        PageImpl<Todo> pageImp = new PageImpl<>(expectedTodoForAdmin, pageable, expectedTodoForAdmin.size());

        when(todoJpaRepository.findAllByName(eq(generateName), eq(pageable))).thenReturn(pageImp);
        when(todoService.getCurrentUser()).thenReturn(currentUserADMIN);
        //when
        Page<Todo> todoPage = searchTodoService.find(new TodoSearchParamsDto(generateName, null), pageable);
        //then
        assertThat(todoPage.getContent()).hasSize(expectedTodoForAdmin.size());
        assertThat(todoPage.getContent()).containsAll(expectedTodoForAdmin);
        verify(todoJpaRepository, times(1)).findAllByName(anyString(), any());
        verify(todoService, times(1)).getCurrentUser();
    }

    private static Stream<Arguments> generateValueByOnlyNameForAdmin() {
        return Stream.of(
                of(listTodos, "nazwa"));
    }

    @ParameterizedTest
    @MethodSource("generateValueByOnlyNameForUser")
    public void findByUser_searchByNameOnly_returnsElementFoundInRepo(List<Todo> expectedTodoForUser, String generateName) {
        //given
        Pageable pageable = PageRequest.of(0, 1);
        PageImpl<Todo> pageImp = new PageImpl<>(expectedTodoForUser, pageable, expectedTodoForUser.size());

        when(todoService.getCurrentUser()).thenReturn(currentUserUSER);
        when(todoJpaRepository.findAllByNameAndUserId
                (eq(generateName), eq(currentUserUSER.getId()), eq(pageable))).thenReturn(pageImp);
        //when
        Page<Todo> todoPage = searchTodoService.find(new TodoSearchParamsDto(generateName, null), pageable);
        //then
        assertThat(todoPage.getContent()).hasSize(expectedTodoForUser.size());
        assertThat(todoPage.getContent()).containsAll(expectedTodoForUser);
        verify(todoJpaRepository, times(1)).findAllByNameAndUserId(anyString(), anyLong(), any());
        verify(todoService, times(2)).getCurrentUser();
    }

    private static Stream<Arguments> generateValueByOnlyNameForUser() {
        return Stream.of(
                of(listTodosForUser, "nazwa"));
    }

    @ParameterizedTest
    @MethodSource("generateValueByOnlyPriorityForAdmin")
    public void findByAdmin_searchByPriorityOnly_returnsElementFoundInRepo(List<Todo> expectedTodoForAdmin, String generatePriority) {
        //given
        Pageable pageable = PageRequest.of(0, 1);
        PageImpl<Todo> pageImp = new PageImpl<>(expectedTodoForAdmin, pageable, expectedTodoForAdmin.size());

        when(todoJpaRepository.findAllByPriority(eq(generatePriority), eq(pageable))).thenReturn(pageImp);
        when(todoService.getCurrentUser()).thenReturn(currentUserADMIN);
        //when
        Page<Todo> todoPage = searchTodoService.find(new TodoSearchParamsDto(null, generatePriority), pageable);
        //then
        assertThat(todoPage.getContent()).hasSize(expectedTodoForAdmin.size());
        assertThat(todoPage.getContent()).containsAll(expectedTodoForAdmin);
        verify(todoJpaRepository, times(1)).findAllByPriority(anyString(), any());
        verify(todoService, times(1)).getCurrentUser();
    }

    private static Stream<Arguments> generateValueByOnlyPriorityForAdmin() {
        return Stream.of(
                of(listTodos, "priorytet"));
    }

    @ParameterizedTest
    @MethodSource("generateValueByOnlyPriorityForUser")
    public void findByUser_searchByPriorityOnly_returnsElementFoundInRepo(List<Todo> expectedTodoForUser, String generatePriority) {
        //given
        Pageable pageable = PageRequest.of(0, 1);
        PageImpl<Todo> pageImp = new PageImpl<>(expectedTodoForUser, pageable, expectedTodoForUser.size());

        when(todoService.getCurrentUser()).thenReturn(currentUserUSER);
        when(todoJpaRepository.findAllByPriorityAndUserId
                (eq(generatePriority), eq(currentUserUSER.getId()), eq(pageable))).thenReturn(pageImp);
        //when
        Page<Todo> todoPage = searchTodoService.find(new TodoSearchParamsDto(null, generatePriority), pageable);
        //then
        assertThat(todoPage.getContent()).hasSize(expectedTodoForUser.size());
        assertThat(todoPage.getContent()).containsAll(expectedTodoForUser);
        verify(todoJpaRepository, times(1)).findAllByPriorityAndUserId(anyString(), anyLong(), any());
        verify(todoService, times(2)).getCurrentUser();
    }

    private static Stream<Arguments> generateValueByOnlyPriorityForUser() {
        return Stream.of(
                of(listTodosForUser, "priorytet"));
    }

    @ParameterizedTest
    @MethodSource("generateValueByBothElementForAdmin")
    public void findByAdmin_searchByBothElement_returnsElementFoundInRepo(List<Todo> expectedTodoForAdmin, String generateName, String generatePriority) {
        //given
        Pageable pageable = PageRequest.of(0, 1);
        PageImpl<Todo> pageImp = new PageImpl<>(expectedTodoForAdmin, pageable, expectedTodoForAdmin.size());

        when(todoJpaRepository.findAllByNameAndPriority(eq(generateName), eq(generatePriority), eq(pageable))).thenReturn(pageImp);
        when(todoService.getCurrentUser()).thenReturn(currentUserADMIN);
        //when
        Page<Todo> todoPage = searchTodoService.find(new TodoSearchParamsDto(generateName, generatePriority), pageable);
        //then
        assertThat(todoPage.getContent()).hasSize(expectedTodoForAdmin.size());
        assertThat(todoPage.getContent()).containsAll(expectedTodoForAdmin);
        verify(todoJpaRepository, times(1)).findAllByNameAndPriority(anyString(), anyString(), any());
        verify(todoService, times(1)).getCurrentUser();
    }

    private static Stream<Arguments> generateValueByBothElementForAdmin() {
        return Stream.of(
                of(listTodos, "nazwa", "priorytet"));
    }

    @ParameterizedTest
    @MethodSource("generateValueByBothElementForUser")
    public void findByUser_searchByBothElement_returnsElementFoundInRepo(List<Todo> expectedTodoForUser, String generateName, String generatePriority) {
        //given
        Pageable pageable = PageRequest.of(0, 1);
        PageImpl<Todo> pageImp = new PageImpl<>(expectedTodoForUser, pageable, expectedTodoForUser.size());

        when(todoService.getCurrentUser()).thenReturn(currentUserUSER);
        when(todoJpaRepository.findAllByNameAndPriorityAndUserId(eq(generateName), eq(generatePriority), eq(currentUserUSER.getId()), eq(pageable))).thenReturn(pageImp);
        //when
        Page<Todo> todoPage = searchTodoService.find(new TodoSearchParamsDto(generateName, generatePriority), pageable);
        //then
        assertThat(todoPage.getContent()).hasSize(expectedTodoForUser.size());
        assertThat(todoPage.getContent()).containsAll(expectedTodoForUser);
        verify(todoJpaRepository, times(1)).findAllByNameAndPriorityAndUserId(anyString(), anyString(), anyLong(), any());
        verify(todoService, times(2)).getCurrentUser();
    }

    private static Stream<Arguments> generateValueByBothElementForUser() {
        return Stream.of(
                of(listTodosForUser, "nazwa", "priorytet"));
    }


    @ParameterizedTest
    @ValueSource(strings = {"  ", "\t", "\n"})
    public void isBlankForAdmin_ShouldReturnEmpty(String input) {
        //given
        Pageable pageable = PageRequest.of(0, 1);
        when(todoService.getCurrentUser()).thenReturn(currentUserADMIN);
        when(todoJpaRepository.findAllByNameAndPriority(eq(input), eq(input), eq(pageable))).thenReturn(Page.empty());
        //when
        Page<Todo> todoPageADMIN = searchTodoService.find(new TodoSearchParamsDto(input, input), pageable);
        //then
        assertThat(todoPageADMIN.getContent()).hasSize(0);
        assertThat(todoPageADMIN.getContent()).containsAll(Page.empty());
        verify(todoJpaRepository, times(1)).findAllByNameAndPriority(anyString(), anyString(), any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"  ", "\t", "\n"})
    public void isBlankForUser_ShouldReturnEmpty(String input) {
        //given
        Pageable pageable = PageRequest.of(0, 1);
        when(todoService.getCurrentUser()).thenReturn(currentUserUSER);
        when(todoJpaRepository.findAllByNameAndPriorityAndUserId(eq(input), eq(input), eq(currentUserUSER.getId()), eq(pageable))).thenReturn(Page.empty());
        //when
        Page<Todo> todoPageUSER = searchTodoService.find(new TodoSearchParamsDto(input, input), pageable);
        //then

        assertThat(todoPageUSER.getContent()).hasSize(0);
        assertThat(todoPageUSER.getContent()).containsAll(Page.empty());
        verify(todoJpaRepository, times(1)).findAllByNameAndPriorityAndUserId(anyString(), anyString(), anyLong(), any());
    }
}
