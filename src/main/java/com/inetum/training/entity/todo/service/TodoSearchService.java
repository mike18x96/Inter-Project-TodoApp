package com.inetum.training.entity.todo.service;

import com.inetum.training.entity.todo.dto.Todo2TodoWithoutUserDtoConverter;
import com.inetum.training.entity.todo.dto.TodoDtoWithoutUser;
import com.inetum.training.entity.todo.model.Todo;
import com.inetum.training.entity.todo.repository.TodoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoSearchService {

    private final TodoJpaRepository todoJpaRepository;
    private final Todo2TodoWithoutUserDtoConverter todo2TodoWithoutUserDtoConverter;

    public Page<TodoDtoWithoutUser> searchByParams(Specification<Todo> todoSpecification, Pageable pageable) {
        Page<Todo> pageTodo = todoJpaRepository.findAll(todoSpecification, pageable);
        return pageTodo.map(todo2TodoWithoutUserDtoConverter::convert);
    }

}
