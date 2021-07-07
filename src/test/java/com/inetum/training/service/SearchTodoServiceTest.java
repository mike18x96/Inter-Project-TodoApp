package com.inetum.training.service;

import com.inetum.training.service.fake.TodoFakeRepositoryImpl;
import com.inetum.training.todo.controller.dto.TodoSearchParamsDto;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.service.SearchTodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class SearchTodoServiceTest {

    private TodoFakeRepositoryImpl fakeRepository;

    private SearchTodoService searchService;

    @BeforeEach
    public void setUp(){
        fakeRepository = new TodoFakeRepositoryImpl();
        searchService = new SearchTodoService(fakeRepository);
    }

    @Test
    public void find_searchByNameOnly_returnsElementFoundInRepo(){
        //given
        Todo todo = new Todo(1L, "nazwa", "priorytet", "opis", true);
        Todo todoWithOtherName = new Todo(1L, "nazwa2", "priorytet", "opis", true);
        fakeRepository.save(todo);
        fakeRepository.save(todoWithOtherName);
        TodoSearchParamsDto searchParams = new TodoSearchParamsDto("nazwa", null);
        //when
        List<Todo> todos = searchService.find(searchParams);
        //then
        assertEquals(1, todos.size());
        assertEquals(todo, todos.get(0));
    }


}
