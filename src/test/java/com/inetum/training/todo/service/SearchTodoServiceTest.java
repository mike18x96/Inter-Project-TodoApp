package com.inetum.training.todo.service;

import com.google.common.collect.Lists;
import com.inetum.training.todo.controller.dto.TodoSearchParamsDto;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.persistance.TodoJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class SearchTodoServiceTest {

    @Mock
    private TodoJpaRepository fakeRepository;

    @InjectMocks
    private SearchTodoService searchService;

    private static final Todo todo_1 = new Todo(1L, "nazwa1", "priorytet1", "opis1", true);
    private static final Todo todo_2 = new Todo(2L, "nazwa2", "priorytet2", "opis2", true);
    private static final Todo todo_3 = new Todo(3L, "nazwa3", "priorytet3", "opis3", true);


    @Test
    public void find_searchByNameOnly_returnsElementFoundInRepo() {
        //given
        Todo todo = new Todo(1L, "nazwa", "priorytet", "opis", true);
        TodoSearchParamsDto searchParams = new TodoSearchParamsDto("nazwa", null);
        when(fakeRepository.findBySearchParams(eq("nazwa"), eq(null)))
                .thenReturn(newArrayList(todo));
        //when
        List<Todo> todos = searchService.find(searchParams);
        //then
        Assertions.assertEquals(1, todos.size());
        Assertions.assertEquals(todo, todos.get(0));
        verify(fakeRepository, times(1))
                .findBySearchParams(anyString(), any());
    }

    @Test
    public void find_searchByPriorityOnly_returnsElementFoundInRepo() {
        //given
        Todo todo = new Todo(1L, "nazwa", "priorytet", "opis", true);
        TodoSearchParamsDto searchParams = new TodoSearchParamsDto(null, "priorytet");
        when(fakeRepository.findBySearchParams(eq(null), eq("priorytet")))
                .thenReturn(newArrayList(todo));
        //when
        List<Todo> todos = searchService.find(searchParams);
        //then
        Assertions.assertEquals(1, todos.size());
        Assertions.assertEquals(todo, todos.get(0));
        verify(fakeRepository, times(1))
                .findBySearchParams(isNull(), eq("priorytet"));
    }

    private static Stream<Arguments> generateDataBothParameters() {
        return Stream.of(
                Arguments.of("nazwa", "priorytet", 1, Lists.newArrayList(todo_1)),
                Arguments.of("nazwa", "priorytet", 2, Lists.newArrayList(todo_2, todo_3)));
    }

    @ParameterizedTest
    @MethodSource("generateDataBothParameters")
    public void find_searchByBothParameters_returnsElementFoundInRepo(String findName, String findPriority, int sizeList, List<Todo> expectedTodo) {
        //given
        TodoSearchParamsDto searchParams = new TodoSearchParamsDto(findName, findPriority);
        when(fakeRepository.findBySearchParams(eq(findName), eq(findPriority)))
                .thenReturn(expectedTodo);

        //when
        List<Todo> todos = searchService.find(searchParams);
        //then
        Assertions.assertEquals(sizeList, todos.size());
        Assertions.assertEquals(expectedTodo, todos);
        verify(fakeRepository, times(1))
                .findBySearchParams(eq("nazwa"), eq("priorytet"));
    }

    private static Stream<Arguments> generateDataNotExistingParameters() {
        return Stream.of(
                Arguments.of("nazwa12", "", 1, Lists.newArrayList(todo_1)),
                Arguments.of("", "priorytet12", 2, Lists.newArrayList(todo_2, todo_3)),
                Arguments.of("  ", "priorytet12", 1, Lists.newArrayList(todo_2)),
                Arguments.of("nazwa12", "  ", 1, Lists.newArrayList(todo_2)));
    }

    @ParameterizedTest
    @MethodSource("generateDataNotExistingParameters")
    public void find_searchByNotExistingParameters_returnsNoElement(String findName, String findPriority, int sizeList, List<Todo> expectedTodo) {
        //given
        TodoSearchParamsDto searchParams = new TodoSearchParamsDto(findName, findPriority);
        when(fakeRepository.findBySearchParams(eq(findName), eq(findPriority)))
                .thenReturn(expectedTodo);

        //when
        List<Todo> todos = searchService.find(searchParams);
        //then
        Assertions.assertEquals(sizeList, todos.size());
        Assertions.assertEquals(expectedTodo, todos);
        verify(fakeRepository, times(1))
                .findBySearchParams(anyString(), anyString());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    public void isBlank_ShouldReturnTrueForNullInputs(String input) {
        //given
        Todo todo = new Todo(1L, "nazwa", "priorytet", "opis", true);
        TodoSearchParamsDto searchParams = new TodoSearchParamsDto(input, input);
        when(fakeRepository.findBySearchParams(eq(input), eq(input)))
                .thenReturn(newArrayList(todo));
        //when
        List<Todo> todos = searchService.find(searchParams);
        //then
        Assertions.assertEquals(1, todos.size());
        Assertions.assertEquals(todo, todos.get(0));
        verify(fakeRepository, times(1))
                .findBySearchParams(any(), any());
    }




}
