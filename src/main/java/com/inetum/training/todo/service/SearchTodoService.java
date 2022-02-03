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

    private final TodoJpaRepository todoJpaRepository;
    private final TodoService todoService;
    private Page<Todo> pageOfTodo;

    public Page<Todo> findByADMIN(TodoSearchParamsDto searchParams, Pageable pageable) {
        if (searchParams.getPriority() == null) {
            pageOfTodo = todoJpaRepository.findAllByName(searchParams.getName(), pageable);
        } else if (searchParams.getName() == null) {
            pageOfTodo = todoJpaRepository.findAllByPriority(searchParams.getPriority(), pageable);
        } else {
            pageOfTodo = todoJpaRepository.findAllByNameAndPriority(searchParams.getName(), searchParams.getPriority(), pageable);
        }
        return pageOfTodo;
    }

    public Page<Todo> findByUSER(TodoSearchParamsDto searchParams, Pageable pageable) {
        Page<Todo> pageOfTodo;
        if (searchParams.getPriority() == null) {
            pageOfTodo = todoJpaRepository.findAllByNameAndUserId(searchParams.getName(), todoService.getCurrentUser().getId(), pageable);
        } else if (searchParams.getName() == null) {
            pageOfTodo = todoJpaRepository.findAllByPriorityAndUserId(searchParams.getPriority(), todoService.getCurrentUser().getId(), pageable);
        } else {
            pageOfTodo = todoJpaRepository.findAllByNameAndPriorityAndUserId(searchParams.getName(), searchParams.getPriority(), todoService.getCurrentUser().getId(), pageable);
        }
        return pageOfTodo;
    }

    public Page<Todo> find(TodoSearchParamsDto searchParams, Pageable pageable) {
        if ((todoService.getCurrentUser().getRole()).equals("ROLE_ADMIN")) {
            return findByADMIN(searchParams, pageable);
        } else {
            return findByUSER(searchParams, pageable);
        }
    }


}
