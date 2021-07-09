package com.inetum.training.todo.service;

import com.inetum.training.todo.controller.dto.TodoSearchParamsDto;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.persistance.TodoJpaRepository;
//import org.junit.jupiter.api.Assertions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class SearchTodoServiceTest {
    private static final int PAGE_NR = 0;
    private static final int PAGE_SIZE = 2;
    private static final String TODO_NAME = "nazwa";

    @Mock
    private TodoJpaRepository fakeRepository;

    @InjectMocks
    private SearchTodoService searchService;


    @Test
    public void find_searchByNameOnly_returnsElementFoundInRepo(){
        //given
        Todo todo = new Todo(1L, TODO_NAME, "priorytet", "opis", true);
        TodoSearchParamsDto searchParams = new TodoSearchParamsDto(TODO_NAME, null, null);
        when( fakeRepository.findBySearchParams( Mockito.eq("nazwa"), any(), any()))
                .thenReturn(new PageImpl<>(newArrayList(todo)));
        //when
        Page<Todo> todos = searchService.find(searchParams);
        //then
        Assertions.assertThat(todos.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(todos.getContent()).contains(todo);
        verify(fakeRepository, times(1))
                .findBySearchParams( anyString(),any() ,any());
        verifyNoMoreInteractions(fakeRepository);
    }

    @Test
    public void find_searchByNameOnly_usesPageableFromSearchParam(){
        //given
        Pageable pageRequest = PageRequest.of(PAGE_NR, PAGE_SIZE);
        TodoSearchParamsDto searchParams = new TodoSearchParamsDto(TODO_NAME, null, pageRequest);
        when( fakeRepository.findBySearchParams( any(), any(), any()))
                .thenReturn(new PageImpl<>(emptyList()));
        //when
        searchService.find(searchParams);
        //then
        verify(fakeRepository, times(1))
                .findBySearchParams( eq("nazwa"), eq(null), eq(pageRequest));
        verifyNoMoreInteractions(fakeRepository);
    }



}
