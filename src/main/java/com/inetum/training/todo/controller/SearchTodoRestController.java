package com.inetum.training.todo.controller;

import com.inetum.training.todo.controller.dto.TodoSearchParamsDto;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.service.SearchTodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search/todos")
@RequiredArgsConstructor
public class SearchTodoRestController {

    private final SearchTodoService searchService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<Todo> getAll(@RequestParam(value="name", required = false) String name,
                             @RequestParam(value="priority", required = false) String priority,
                             Pageable pageable){
        return searchService.find(new TodoSearchParamsDto(name, priority, pageable));
    }
}
