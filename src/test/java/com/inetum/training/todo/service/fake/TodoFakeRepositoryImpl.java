//package com.inetum.training.todo.service.fake;
//
//
//import com.inetum.training.todo.domain.Todo;
//import com.inetum.training.todo.persistance.TodoJpaRepository;
//import org.springframework.dao.EmptyResultDataAccessException;
//
//import javax.persistence.EntityNotFoundException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//public class TodoFakeRepositoryImpl implements TodoJpaRepository {
//
//    private final Map<Long, Todo> todoMap = new HashMap<>();
//    private static Long lastId = 0L;
//
//
//    @Override
//    public Todo save(Todo todo) {
//
//        if(todo.getId()==null){
//            lastId= Long.valueOf(todoMap.size());
//            todo.setId(++lastId);
//            todoMap.put(lastId, todo);
//        }else{
//            todoMap.put(todo.getId(),todo);
//        }
//        return todo;
//    }
//
//    @Override
//    public List<Todo> findBySearchParams(String name, String priority) {
//        Stream<Todo> todoStream = todoMap.values().stream();
//        if (stringNotEmpty(name)) {
//            todoStream = todoStream.filter(value -> name.equals(value.getName()));
//        }
//        if (stringNotEmpty(priority)) {
//            todoStream = todoStream.filter(value -> priority.equals(value.getPriority()));
//        }
//        return todoStream.collect(Collectors.toList());
//    }
//
//    private static boolean stringNotEmpty(String str) {
//        return str != null && str.length() > 0;
//    }
//
//
//    @Override
//    public <S extends Todo> Iterable<S> saveAll(Iterable<S> iterable) {
//        return null;
//    }
//
//    @Override
//    public Optional<Todo> findById(Long aLong) {
//
//
//        return Optional.ofNullable(todoMap.get(aLong));
//
//    }
//
//    @Override
//    public boolean existsById(Long aLong) {
//        if (todoMap.get(aLong) == null) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    @Override
//    public Iterable<Todo> findAll() {
//        return todoMap.values();
//    }
//
//    @Override
//    public Iterable<Todo> findAllById(Iterable<Long> iterable) {
//        return null;
//    }
//
//    @Override
//    public long count() {
//        return 0;
//    }
//
//    @Override
//    public void deleteById(Long aLong) {
//        if (todoMap.get(aLong) == null) {
//            throw new EntityNotFoundException();
//        }else{
//            todoMap.remove(aLong);
//        }
//    }
//
//
//    @Override
//    public void delete(Todo todo) {
//
//    }
//
//    @Override
//    public void deleteAllById(Iterable<? extends Long> iterable) {
//
//    }
//
//    @Override
//    public void deleteAll(Iterable<? extends Todo> iterable) {
//
//    }
//
//
//    @Override
//    public void deleteAll() {
//
//    }
//}
