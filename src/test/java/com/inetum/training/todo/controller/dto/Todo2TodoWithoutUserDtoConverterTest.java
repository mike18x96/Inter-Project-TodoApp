//package com.inetum.training.todo.controller.dto;
//
//import com.inetum.training.todo.domain.Todo;
//import com.inetum.training.todo.service.SearchTodoService;
//import com.inetum.training.user.domain.User;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//class Todo2TodoWithoutUserDtoConverterTest {
//
//    @Mock
//    private Todo2TodoWithoutUserDtoConverter todo2TodoWithoutUserDtoConverter;
//
//    @Test
//    void convert() {
//        //given
//        User user = new User(1L, "nazwa", "password", User.Role.ADMIN);
//        Todo todo = new Todo(1l, "nazwa","priorytet", "opis", false, user);
//        TodoDtoWithoutUser expectedTodoDtoWithoutUser =  new TodoDtoWithoutUser(1l, "nazwa","priorytet", "opis", false, 1l);
//
//        when(todo2TodoWithoutUserDtoConverter.convert(eq(todo))).thenReturn(expectedTodoDtoWithoutUser);
//
//        //when
//        TodoDtoWithoutUser todoDtoWithoutUser = todo2TodoWithoutUserDtoConverter.convert(todo);
//
//        //then
//
//
//
//
//    }
//
//}
