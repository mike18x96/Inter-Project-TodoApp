package com.inetum.training.todo.service;

import com.inetum.training.todo.controller.dto.TodoSearchParamsDto;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.persistance.TodoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchTodoService {

    private final TodoJpaRepository todoRepository;

    public Page<Todo> find(TodoSearchParamsDto searchParams) {
        Page<Todo> pageOfTodo;
        if (searchParams.getPriority() == null && searchParams.getName() == null) {
            pageOfTodo = todoRepository.findAll(searchParams.getPageable());
        } else if (searchParams.getPriority() == null) {
            pageOfTodo = todoRepository.findAllByNameContaining(searchParams.getName(), searchParams.getPageable());
        } else if (searchParams.getName() == null) {
            pageOfTodo = todoRepository.findAllByPriorityContaining(searchParams.getPriority(), searchParams.getPageable());
        } else {
            pageOfTodo = todoRepository.findAllByNameAndPriorityContaining(searchParams.getName(), searchParams.getPriority(), searchParams.getPageable());
        }
        return pageOfTodo;
    }

}
