package com.inetum.training.todo.controller;

import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoRestController {

    private final TodoService todoService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<Todo> getAll(Pageable pageable) {
        return todoService.findAll(pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid Todo todo) {
        todo.setId(null);
        return todoService.save(todo);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Optional<Todo> get(@PathVariable("id") Long id) {
        return todoService.findById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@RequestBody @Valid Todo todo, @PathVariable("id") Long id) {
        todoService.update(todo, id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        todoService.delete(id);
    }

}