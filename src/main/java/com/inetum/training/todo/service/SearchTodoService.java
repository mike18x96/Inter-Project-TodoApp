package com.inetum.training.todo.service;

import com.inetum.training.todo.controller.dto.TodoSearchParamsDto;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.persistance.TodoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchTodoService {

    private final TodoJpaRepository todoRepository;

    public Page<Todo> find(TodoSearchParamsDto searchParams, Pageable pageable) {
        Page<Todo> pageOfTodo;
        if (searchParams.getPriority() == null) {
            pageOfTodo = todoRepository.findAllByName(searchParams.getName(), pageable);
        } else if (searchParams.getName() == null) {
            pageOfTodo = todoRepository.findAllByPriority(searchParams.getPriority(), pageable);
        } else {
            pageOfTodo = todoRepository.findAllByNameAndPriority(searchParams.getName(), searchParams.getPriority(), pageable);
        }
        return pageOfTodo;
    }

}
