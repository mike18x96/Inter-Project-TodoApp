package com.inetum.training.todo.controller;

import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.persistance.TodoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoRestController {

    private final TodoJpaRepository todoRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Todo> getAll(){
        Iterable<Todo> todos = todoRepository.findAll();
        return iterable2list(todos);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody Todo todo){
        todo.setId(null);
        return todoRepository.save(todo).getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Todo get(@PathVariable("id") Long id){
        return todoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@RequestBody Todo todo, @PathVariable("id") Long id){
        todo.setId(id);
        todoRepository.save(todo);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id){
        todoRepository.deleteById(id);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String notFoundHandler(){
        return "nie znaleziono elementu o podanym id";
    }

    private List<Todo> iterable2list(Iterable<Todo> todos) {
        List<Todo> result = new ArrayList<>();
        todos.forEach( t -> result.add(t));
        return result;
    }
}