package com.inetum.training.todo.service;

import com.inetum.training.todo.controller.dto.TodoSearchParamsDto;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.persistance.TodoJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SearchTodoServiceTest {

    @Mock
    private TodoJpaRepository fakeRepository;

    @InjectMocks
    private SearchTodoService searchService;


    @Test
    public void find_searchByNameOnly_returnsElementFoundInRepo(){
        //given
        Todo todo = new Todo(1L, "nazwa", "priorytet", "opis", true);
        TodoSearchParamsDto searchParams = new TodoSearchParamsDto("nazwa", null);
        Mockito.when( fakeRepository.findBySearchParams( Mockito.eq("nazwa"), Mockito.eq(null)))
                .thenReturn(newArrayList(todo));
        //when
        List<Todo> todos = searchService.find(searchParams);
        //then
        Assertions.assertEquals(1, todos.size());
        Assertions.assertEquals(todo, todos.get(0));
        Mockito.verify(fakeRepository, Mockito.times(1))
                .findBySearchParams( Mockito.anyString(), Mockito.any());
//        Mockito.verifyNoMoreInteractions(fakeRepository);
    }


}
