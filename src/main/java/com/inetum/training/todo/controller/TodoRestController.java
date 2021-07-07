package com.inetum.training.todo.controller;

import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.persistance.TodoRepository;
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
    public Long create(@RequestBody Todo todo){
        return TodoRepository.insert(todo);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Todo get(@PathVariable("id") Long id){
        return TodoRepository.get(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@RequestBody Todo todo, @PathVariable("id") Long id){
        todo.setId(id);
        TodoRepository.update(todo);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id){
        TodoRepository.delete(id);
    }
}
