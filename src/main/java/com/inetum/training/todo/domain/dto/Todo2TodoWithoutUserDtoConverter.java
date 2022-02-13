package com.inetum.training.todo.domain.dto;

import com.inetum.training.todo.domain.Todo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class Todo2TodoWithoutUserDtoConverter implements Converter<Todo, TodoDtoWithoutUser> {
    @Override
    public TodoDtoWithoutUser convert(Todo todo) {

        return new TodoDtoWithoutUser(todo.getId(), todo.getName(), todo.getPriority(), todo.getDescription(), todo.isCompleted(), todo.getUser().getId());
    }
}
