package com.inetum.training.todo.service;

import com.inetum.training.security.components.LoggedCurrentUser;
import com.inetum.training.todo.domain.dto.Todo2TodoWithoutUserDtoConverter;
import com.inetum.training.todo.domain.dto.TodoDtoWithoutUser;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.persistance.TodoJpaRepository;
import com.inetum.training.user.persistance.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoJpaRepository todoJpaRepository;
    private final UserRepository userRepository;
    private final LoggedCurrentUser loggedCurrentUser;

    public Page<TodoDtoWithoutUser> findAll(Pageable pageable) {
        Page<Todo> todoPage;
        Todo2TodoWithoutUserDtoConverter todo2TodoWithoutUserDtoConverter = new Todo2TodoWithoutUserDtoConverter();
        if ((loggedCurrentUser.getCurrentUser().getRole()).equals("ROLE_USER")) {
            todoPage = todoJpaRepository.findAllByUserId(loggedCurrentUser.getCurrentUser().getId(), pageable);
        } else {
            todoPage = todoJpaRepository.findAll(pageable);
        }
        return todoPage.map(todo -> todo2TodoWithoutUserDtoConverter.convert(todo));
    }

    public TodoDtoWithoutUser findById(Long id) {

        if (todoJpaRepository.existsById(id)) {
            Todo2TodoWithoutUserDtoConverter todo2TodoWithoutUserDtoConverter = new Todo2TodoWithoutUserDtoConverter();
            Todo todo = null;
            if ((loggedCurrentUser.getCurrentUser().getRole()).equals("ROLE_ADMIN")) {
                todo = todoJpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id.toString()));
            }
            if ((loggedCurrentUser.getCurrentUser().getId() == todoJpaRepository.findById(id).get().getUser().getId())) {
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

    public Long save(Todo todo) {
        todo.setUser(userRepository.getById(loggedCurrentUser.getCurrentUser().getId()));
        return todoJpaRepository.save(todo).getId();
    }

    public String update(Todo todo, Long id) {
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

    public void deleteById(Long id) {
        if (todoJpaRepository.existsById(id)) {
            if ((loggedCurrentUser.getCurrentUser().getRole()).equals("ROLE_ADMIN")) {
                todoJpaRepository.deleteById(id);
            }else{
                if ((loggedCurrentUser.getCurrentUser().getId() == todoJpaRepository.findById(id).get().getUser().getId())) {
                    todoJpaRepository.deleteById(id);
                }
            }
        }
    }
}