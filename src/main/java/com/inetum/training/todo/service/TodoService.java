package com.inetum.training.todo.service;

import com.inetum.training.security.model.CurrentUser;
import com.inetum.training.todo.controller.dto.Todo2TodoWithoutUserDtoConverter;
import com.inetum.training.todo.controller.dto.TodoDtoWithoutUser;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.persistance.TodoJpaRepository;
import com.inetum.training.user.persistance.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoJpaRepository todoJpaRepository;
    private final UserRepository userRepository;
//    private final Todo2TodoWithoutUserDtoConverter todo2TodoWithoutUserDtoConverter;


    public Page<TodoDtoWithoutUser> findAll(Pageable pageable) {
        Page<Todo> todoPageOnlyUser = todoJpaRepository.findAllByUserId(getCurrentUser().getId(), pageable);
        Page<Todo> todoPageUserAdmin = todoJpaRepository.findAll(pageable);
        Todo2TodoWithoutUserDtoConverter todo2TodoWithoutUserDtoConverter = new Todo2TodoWithoutUserDtoConverter();
        if ((getCurrentUser().getRole()).equals("ROLE_USER")) {
            return todoPageOnlyUser.map(todo -> todo2TodoWithoutUserDtoConverter.convert(todo));
        } else {
            return todoPageUserAdmin.map(todo -> todo2TodoWithoutUserDtoConverter.convert(todo));
        }
    }

    public TodoDtoWithoutUser findById(Long id) {

        if (todoJpaRepository.existsById(id)) {
            Todo2TodoWithoutUserDtoConverter todo2TodoWithoutUserDtoConverter = new Todo2TodoWithoutUserDtoConverter();
            Todo todo = null;
            if ((getCurrentUser().getRole()).equals("ROLE_ADMIN")) {
                todo = todoJpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id.toString()));
            }
            if ((getCurrentUser().getId() == todoJpaRepository.findById(id).get().getUser().getId())) {
                todo = todoJpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id.toString()));
            }
            return todo2TodoWithoutUserDtoConverter.convert(todo);

        } else {
            throw new EntityNotFoundException();
        }

    }

    public boolean existsById(Long id) {
        return todoJpaRepository.existsById(id);
    }

    public void deleteById(Long id) {
        if (todoJpaRepository.existsById(id)) {
            if ((getCurrentUser().getRole()).equals("ROLE_ADMIN")) {
                todoJpaRepository.deleteById(id);
            }
            if ((getCurrentUser().getId() == todoJpaRepository.findById(id).get().getUser().getId())) {
                todoJpaRepository.deleteById(id);
            }
        }
    }

    public Long save(Todo todo) {
        todo.setUser(userRepository.getById(getCurrentUser().getId()));
        return todoJpaRepository.save(todo).getId();
    }

    public String update(Todo todo, Long id){
        if (existsById(id)) {
            todo.setId(id);
            save(todo);
        } else {
            throw new EntityNotFoundException();
        }
        return "Todo updated!";
    }

    public String delete(Long id) {
        if (existsById(id)) {
            deleteById(id);
        } else {
            throw new EntityNotFoundException();
        }
        return "Todo deleted";
    }

    CurrentUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (CurrentUser) auth.getPrincipal();
    }

}
