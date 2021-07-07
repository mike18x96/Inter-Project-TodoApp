package com.inetum.training.todo.controller;

import com.inetum.training.todo.controller.dto.TodoSearchParamsDto;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.persistance.TodoRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search/todos")
public class SearchTodoRestController {

    @RequestMapping(method = RequestMethod.GET)
    public List<Todo> getAll(@RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "priority", required = false) String priority) {
        return TodoRepository.find(new TodoSearchParamsDto(name, priority));
    }
}
