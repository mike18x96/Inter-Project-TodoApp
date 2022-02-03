package com.inetum.training.todo.service;

import com.inetum.training.security.model.CurrentUser;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.persistance.TodoJpaRepository;
import com.inetum.training.user.persistance.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoJpaRepository todoJpaRepository;
    private final UserRepository userRepository;

    public Page<Todo> findAll(Pageable pageable) {
        if ((getCurrentUser().getRole()).equals("ROLE_USER")) {
            return todoJpaRepository.findAllByUserId(getCurrentUser().getId(), pageable);
        } else {
            return todoJpaRepository.findAll(pageable);
        }
    }

    public Optional<Todo> findById(Long id) {
        if (todoJpaRepository.existsById(id)) {
            if ((getCurrentUser().getRole()).equals("ROLE_ADMIN")) {
                return todoJpaRepository.findById(id);
            }
            if ((getCurrentUser().getId() == todoJpaRepository.findById(id).get().getUser().getId())) {
                return todoJpaRepository.findById(id);
            }
        }else{
            throw new EntityNotFoundException();
        }
        return null;
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

    public void update(Todo todo, Long id) {
        if (existsById(id)) {
            todo.setId(id);
            save(todo);
        } else {
            throw new EntityNotFoundException();
        }
    }

    public void delete(Long id) {
        if (existsById(id)) {
            deleteById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String notFoundHandler() {
        return "nie znaleziono elementu o podanym id";
    }

    CurrentUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (CurrentUser) auth.getPrincipal();
    }

}
