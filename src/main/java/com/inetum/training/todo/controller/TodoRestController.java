package com.inetum.training.todo.controller;


import com.inetum.training.persistance.exception.EntityNotFoundException;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.persistance.TodoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoRestController {

    @RequestMapping(method = RequestMethod.GET)
    public List<Todo> getAll(){
        return TodoRepository.getAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody Todo todo){
        return TodoRepository.insert(todo);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Todo get(@PathVariable("id") Long id){
        Todo result = TodoRepository.get(id);
        if(result == null){
            throw new EntityNotFoundException();
        }
        return result;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@RequestBody Todo todo, @PathVariable("id") Long id){
        todo.setId(id);
        TodoRepository.update(todo);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id){
        TodoRepository.delete(id);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String notFoundHandler(){
        return "nie znaleziono elementu o podanym id";
    }
}