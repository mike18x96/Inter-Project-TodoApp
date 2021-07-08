package com.inetum.training.todo.service;

import com.google.common.collect.Lists;
import com.inetum.training.todo.controller.dto.TodoSearchParamsDto;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.persistance.TodoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchTodoService {

    private final TodoJpaRepository todoRepository;

    public List<Todo> find(TodoSearchParamsDto searchParams){
        return Lists.newArrayList(todoRepository.findBySearchParams(searchParams.getName(),searchParams.getPriority()));
    }

//    private List<Todo> iterable2list(Iterable<Todo> todos) {
//        List<Todo> result = new ArrayList<>();
//        todos.forEach( t -> result.add(t));
//        return result;
//    }
}
