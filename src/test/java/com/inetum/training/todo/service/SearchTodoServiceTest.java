package com.inetum.training.todo.service;

import com.google.common.collect.Lists;
import com.inetum.training.todo.controller.dto.TodoSearchParamsDto;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.persistance.TodoJpaRepository;
import org.junit.jupiter.api.Test;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchTodoServiceTest {

    @Mock
    private TodoJpaRepository fakeRepository;

    @InjectMocks
    private SearchTodoService searchService;

    private static final Todo todo_1 = new Todo(1L, "nazwa", "priorytet", "opis1", true);
    private static final Todo todo_2 = new Todo(2L, "nazwa", "priorytet", "opis2", true);
    private static final Todo todo_3 = new Todo(3L, "nazwa", "priorytet", "opis3", true);
    private static final List<Todo> listTodo = Arrays.asList(todo_1, todo_2, todo_3);

    @Test
    public void find_searchByNameOnly_returnsElementFoundInRepo() {
        //given
        Pageable pageable = PageRequest.of(0, 2);
        PageImpl<Todo> pageImp = new PageImpl<>(listTodo, pageable, listTodo.size());
        TodoSearchParamsDto searchParams = new TodoSearchParamsDto("nazwa", null);
        when(fakeRepository.findAllByName(eq("nazwa"), eq(pageable)))
                .thenReturn(pageImp);
        //when
        Page<Todo> todoPage = searchService.find(searchParams, pageable);
        //then
        assertThat(todoPage.getContent()).hasSize(3);
        assertThat(todoPage.getContent()).containsAll(listTodo);
        verify(fakeRepository, times(1))
                .findAllByName(anyString(), any(Pageable.class));
    }

    @Test
    public void find_searchByPriorityOnly_returnsElementFoundInRepo() {
        //given
        Pageable pageable = PageRequest.of(0, 2);
        PageImpl<Todo> pageImp = new PageImpl<>(listTodo, pageable, listTodo.size());
        TodoSearchParamsDto searchParams = new TodoSearchParamsDto(null,"priorytet");
        when(fakeRepository.findAllByPriority(eq("priorytet"), eq(pageable)))
                .thenReturn(pageImp);
        //when
        Page<Todo> todoPage = searchService.find(searchParams, pageable);
        //then
        assertThat(todoPage.getContent()).hasSize(3);
        assertThat(todoPage.getContent()).containsAll(listTodo);
        verify(fakeRepository, times(1))
                .findAllByPriority(anyString(), any(Pageable.class));
    }

    @Test
    public void find_searchByBothParameters_returnsElementFoundInRepo() {
        //given
        Pageable pageable = PageRequest.of(0, 2);
        PageImpl<Todo> pageImp = new PageImpl<>(listTodo, pageable, listTodo.size());
        TodoSearchParamsDto searchParams = new TodoSearchParamsDto("nazwa", "priorytet");
        when(fakeRepository.findAllByNameAndPriority(eq("nazwa"), eq("priorytet"), eq(pageable)))
                .thenReturn(pageImp);
        //when
        Page<Todo> todoPage = searchService.find(searchParams, pageable);
        //then
        assertThat(todoPage.getContent()).hasSize(3);
        assertThat(todoPage.getContent()).containsAll(listTodo);
        verify(fakeRepository, times(1))
                .findAllByNameAndPriority(anyString(), anyString(), any(Pageable.class));
    }


    @ParameterizedTest
    @MethodSource("generateEmptyData")
    public void find_searchByNotExistingParameters_returnsNoElement(String findName, String findPriority, int expectedElements, List<Todo> expectedListTodo) {
        //given
        Pageable pageable = PageRequest.of(0, 2);
        PageImpl<Todo> pageImp = new PageImpl<>(expectedListTodo, pageable, expectedListTodo.size());
        TodoSearchParamsDto searchParams = new TodoSearchParamsDto(findName, findPriority);
        when(fakeRepository.findAllByNameAndPriority(eq(findName), eq(findPriority), eq(pageable)))
                .thenReturn(pageImp);
        //when
        Page<Todo> todoPage = searchService.find(searchParams, pageable);
        //then
        assertThat(todoPage.getContent()).hasSize(expectedElements);
        assertThat(todoPage.getContent()).containsAll(expectedListTodo);
        verify(fakeRepository, times(1))
                .findAllByNameAndPriority(anyString(), anyString(), any(Pageable.class));
    }

    private static Stream<Arguments> generateEmptyData() {
        return Stream.of(
                Arguments.of("nazwa1", "priorytet1", 0, Lists.newArrayList()),
                Arguments.of("nazwa2", "priorytet2", 0, Lists.newArrayList()),
                Arguments.of("nazwa3", "priorytet3", 0, Lists.newArrayList()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"  ", "\t", "\n"})
    public void isBlank_ShouldReturnEmpty(String input) {
        //given
        Pageable pageable = PageRequest.of(0, 2);
        //PageImpl<Todo> pageImp = new PageImpl<>(listTodo, pageable, listTodo.size());
        TodoSearchParamsDto searchParams = new TodoSearchParamsDto(input, input);
        when(fakeRepository.findAllByNameAndPriority(eq(input), eq(input), eq(pageable)))
                .thenReturn(Page.empty());
        //when
        Page<Todo> todoPage = searchService.find(searchParams, pageable);
        //then
        assertThat(todoPage.getContent()).hasSize(0);
        assertThat(todoPage.getContent()).containsAll(Page.empty());
        verify(fakeRepository, times(1))
                .findAllByNameAndPriority(anyString(), anyString(), any(Pageable.class));
    }


}
