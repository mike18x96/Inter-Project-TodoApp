package com.inetum.training.todo.controller;

import com.inetum.training.todo.controller.dto.TodoDtoWithoutUser;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoRestController {

    private final TodoService todoService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<TodoDtoWithoutUser> getAll(Pageable pageable) {
        return todoService.findAll(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid Todo todo) {
        todo.setId(null);
        return todoService.save(todo);
    }

    @GetMapping("/{id}")
    public TodoDtoWithoutUser get(@PathVariable("id") Long id) {
        try {
            return todoService.findById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(id.toString());
        }
    }

    @PutMapping("/{id}")
    public void update(@RequestBody @Valid Todo todo, @PathVariable("id") Long id) {
        try {
            todoService.update(todo, id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(id.toString());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        try {
            todoService.delete(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(id.toString());
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String notFoundHandler(EntityNotFoundException e) {
        return String.format("Not found element with id: %s", e.getMessage());
    }

}