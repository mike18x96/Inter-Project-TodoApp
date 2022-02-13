package com.inetum.training.todo.service;

import com.inetum.training.todo.domain.dto.Todo2TodoWithoutUserDtoConverter;
import com.inetum.training.todo.domain.dto.TodoDtoWithoutUser;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.persistance.TodoJpaRepository;
import com.inetum.training.todo.persistance.specyfication.TodoSpecifications;
import com.inetum.training.todo.persistance.specyfication.TodoSpecificationsBuilder;
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
import org.springframework.data.jpa.domain.Specification;

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
    private Todo2TodoWithoutUserDtoConverter todo2TodoWithoutUserDtoConverter;

    @InjectMocks
    private SearchTodoService searchTodoService;

    private static final User USER_TEST = new User(1L, "henio", "henio", USER);
    private static final User ADMIN_TEST = new User(2L, "admin", "admin", ADMIN);

    private static final Todo TODO_1 = new Todo(1L, "nazwa", "priorytet", "opis", true, USER_TEST);
    private static final Todo TODO_2 = new Todo(2L, "nazwa", "priorytet", "opis", true, ADMIN_TEST);
    private static final List<Todo> TODO_LIST = Arrays.asList(TODO_1, TODO_2);

    private static final TodoDtoWithoutUser TODO_DTO_1 = new TodoDtoWithoutUser(1L, "nazwa", "priorytet", "opis", true, USER_TEST.getId());
    private static final TodoDtoWithoutUser TODO_DTO_2 = new TodoDtoWithoutUser(2L, "nazwa", "priorytet", "opis", true, ADMIN_TEST.getId());
    private static final List<TodoDtoWithoutUser> TODO_DTO_LIST = Arrays.asList(TODO_DTO_1, TODO_DTO_2);

    @ParameterizedTest
    @MethodSource("generateValueByOnlyName")
    public void searchByParams_searchByNameOnly_returnsElementFoundInRepo(List<Todo> expectedTodo, List<TodoDtoWithoutUser> expectedTodoDto, String generateName) {
        //given
        TodoSpecificationsBuilder todoSpecificationsBuilder = new TodoSpecificationsBuilder();
        todoSpecificationsBuilder.with("name", "=", generateName);
        Specification<Todo> specification = todoSpecificationsBuilder.build();
        Pageable pageable = PageRequest.of(0, 2);
        PageImpl<Todo> pageImp = new PageImpl<>(expectedTodo, pageable, expectedTodo.size());

        when(todoJpaRepository.findAll(eq(specification), eq(pageable))).thenReturn(pageImp);
        convertAllToDto(expectedTodo, expectedTodoDto);

        //when
        Page<TodoDtoWithoutUser> todoPage = searchTodoService.searchByParams(specification, pageable);
        //then
        assertThat(todoPage.getContent()).hasSize(expectedTodo.size());
        assertThat(todoPage.getContent()).containsAll(expectedTodoDto);
        verify(todoJpaRepository, times(1)).findAll(any(TodoSpecifications.class), any(Pageable.class));
        verify(todo2TodoWithoutUserDtoConverter, times(expectedTodo.size())).convert(any(Todo.class));
    }

    private static Stream<Arguments> generateValueByOnlyName() {
        return Stream.of(
                of(TODO_LIST, TODO_DTO_LIST, "nazwa"));
    }

    @ParameterizedTest
    @MethodSource("generateValueByOnlyPriority")
    public void searchByParams_searchByPriorityOnly_returnsElementFoundInRepo(List<Todo> expectedTodo, List<TodoDtoWithoutUser> expectedTodoDto, String generatePriority) {
        //given
        TodoSpecificationsBuilder todoSpecificationsBuilder = new TodoSpecificationsBuilder();
        todoSpecificationsBuilder.with("priority", "=", generatePriority);
        Specification<Todo> specification = todoSpecificationsBuilder.build();

        Pageable pageable = PageRequest.of(0, 1);
        PageImpl<Todo> pageImp = new PageImpl<>(expectedTodo, pageable, expectedTodo.size());

        when(todoJpaRepository.findAll(eq(specification), eq(pageable))).thenReturn(pageImp);
        convertAllToDto(expectedTodo, expectedTodoDto);

        //when
        Page<TodoDtoWithoutUser> todoPage = searchTodoService.searchByParams(specification, pageable);
        //then
        assertThat(todoPage.getContent()).hasSize(expectedTodo.size());
        assertThat(todoPage.getContent()).containsAll(expectedTodoDto);
        verify(todoJpaRepository, times(1)).findAll(any(TodoSpecifications.class), any(Pageable.class));
        verify(todo2TodoWithoutUserDtoConverter, times(expectedTodo.size())).convert(any(Todo.class));
    }

    private static Stream<Arguments> generateValueByOnlyPriority() {
        return Stream.of(
                of(TODO_LIST, TODO_DTO_LIST, "priorytet"));
    }

    @ParameterizedTest
    @MethodSource("generateValueByBothElement")
    public void findByAdmin_searchByBothElement_returnsElementFoundInRepo(List<Todo> expectedTodo, List<TodoDtoWithoutUser> expectedTodoDto, String generateName, String generatePriority) {
        //given
        TodoSpecificationsBuilder todoSpecificationsBuilder = new TodoSpecificationsBuilder();
        todoSpecificationsBuilder.with("name", "=", generateName);
        todoSpecificationsBuilder.with("priority", "=", generatePriority);
        Specification<Todo> specification = todoSpecificationsBuilder.build();
        Pageable pageable = PageRequest.of(0, 1);
        PageImpl<Todo> pageImp = new PageImpl<>(expectedTodo, pageable, expectedTodo.size());
        when(todoJpaRepository.findAll(eq(specification), eq(pageable))).thenReturn(pageImp);
        convertAllToDto(expectedTodo, expectedTodoDto);
        //when
        Page<TodoDtoWithoutUser> todoPage = searchTodoService.searchByParams(specification, pageable);
        //then
        assertThat(todoPage.getContent()).hasSize(expectedTodoDto.size());
        assertThat(todoPage.getContent()).containsAll(expectedTodoDto);
        verify(todoJpaRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        verify(todo2TodoWithoutUserDtoConverter, times(expectedTodo.size())).convert(any(Todo.class));
    }

    private static Stream<Arguments> generateValueByBothElement() {
        return Stream.of(
                of(TODO_LIST, TODO_DTO_LIST, "nazwa", "priorytet"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"  ", "\t", "\n"})
    public void isBlank_ShouldReturnEmpty(String input) {
        TodoSpecificationsBuilder todoSpecificationsBuilder = new TodoSpecificationsBuilder();
        todoSpecificationsBuilder.with("name", "=", input);
        todoSpecificationsBuilder.with("priority", "=", input);
        Specification<Todo> specification = todoSpecificationsBuilder.build();
        Pageable pageable = PageRequest.of(0, 2);
        when(todoJpaRepository.findAll(eq(specification), eq(pageable))).thenReturn(Page.empty());
        //when
        Page<TodoDtoWithoutUser> todoPage = searchTodoService.searchByParams(specification, pageable);
        //then
        assertThat(todoPage.getContent()).hasSize(0);
        assertThat(todoPage.getContent()).containsAll(Page.empty());
        verify(todoJpaRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    private void convertAllToDto(List<Todo> expectedTodos, List<TodoDtoWithoutUser> expectedDtos) {
        for (int i = 0; i < expectedTodos.size(); i++) {
            when(todo2TodoWithoutUserDtoConverter.convert(expectedTodos.get(i))).thenReturn(expectedDtos.get(i));
        }
    }

}
