package com.inetum.training.todo.persistance;


import com.inetum.training.todo.controller.dto.TodoSearchParamsDto;
import com.inetum.training.todo.domain.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TodoRepository {

    private static Map<Long, Todo> todoMap = new ConcurrentHashMap<>();
    private static Long lastId = 0L;

    static {
        todoMap.put(1L, new Todo(1L, "zadanie 1", "wysoki", "Rozwiązać zadanie 1 zgodnie z poleceniami", true));
        todoMap.put(2L, new Todo(2L, "zadanie 2", "wysoki", "Rozwiązać zadanie 2", false));
        lastId = 2L;
    }

    public static List<Todo> getAll(){
        return new ArrayList<>(todoMap.values());
    }

    public static Todo get(Long id){
        return todoMap.get(id);
    }

    public static void delete(Long id){
        todoMap.remove(id);
    }

    public static void update(Todo todo){
        todoMap.put(todo.getId(), todo);
    }

    public static Long insert(Todo todo){
        while(todoMap.containsKey(lastId)) {
            lastId++;
        }
        todo.setId(lastId);
        todoMap.put(lastId, todo);
        return lastId;
    }

    public static List<Todo> find(TodoSearchParamsDto searchParams){
        Stream<Todo> todoStream = todoMap.values().stream();
        if(stringNotEmpty(searchParams.getName())){
            todoStream = todoStream.filter( value -> searchParams.getName().equals(value.getName()));
        }
        if(stringNotEmpty(searchParams.getPriority())){
            todoStream = todoStream.filter( value -> searchParams.getPriority().equals(value.getPriority()));
        }
        return todoStream.collect(Collectors.toList());
    }

    private static boolean stringNotEmpty(String str) {
        return str != null && str.length()>0;
    }

}
