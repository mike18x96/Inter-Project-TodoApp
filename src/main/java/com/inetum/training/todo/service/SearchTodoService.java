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

    public Page<Todo> find(TodoSearchParamsDto searchParams){
        return todoRepository.findBySearchParams(searchParams.getName(),searchParams.getPriority(), searchParams.getPageRequest());
    }

}
