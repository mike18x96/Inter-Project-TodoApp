package com.inetum.training.todo.controller;

import com.inetum.training.entity.todo.controller.TodoController;
import com.inetum.training.entity.todo.dto.TodoDtoWithoutUser;
import com.inetum.training.entity.todo.model.Todo;
import com.inetum.training.entity.todo.service.TodoService;
import com.inetum.training.entity.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Stream;

import static com.inetum.training.TestJsonUtils.convertObjectToJson;
import static com.inetum.training.entity.user.model.User.Role.USER;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
public class StandaloneTodoControllerTest {

    @Mock
    private TodoService todoService;

    private MockMvc mockMvc;
    public static final String URL = "/todos";

    private static final User USER_TEST_USER = new User(2L, "henio", "henio", USER);
    private static final TodoDtoWithoutUser TODO_DTO_1 = new TodoDtoWithoutUser(1L, "nazwa", "priorytet", "opis", true, USER_TEST_USER.getId());
    private static final TodoDtoWithoutUser TODO_DTO_2 = new TodoDtoWithoutUser(2L, "nazwa", "priorytet", "opis", false, USER_TEST_USER.getId());

    @BeforeEach
    public void setUp() {
        mockMvc = standaloneSetup(new TodoController(todoService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    public void getAll_TodoFound_returnsTodo() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 2);
        //when
        mockMvc.perform(get(URL)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isOk());
        verify(todoService, times(1)).findAll(pageable);
    }

    @ParameterizedTest
    @MethodSource("generateParamToGetOne")
    public void getOne_TodoFound_returns200(Long id, TodoDtoWithoutUser expectedTodoDto) throws Exception {
        //given
        when(todoService.findById(id)).thenReturn(expectedTodoDto);
        //when
        mockMvc.perform(get(URL + "/" + id)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(jsonPath("$.name", is(expectedTodoDto.getName())))
                .andExpect(jsonPath("$.priority", is(expectedTodoDto.getPriority())))
                .andExpect(content().contentType(APPLICATION_JSON));
        verify(todoService, times(1)).findById(id);
        verifyNoMoreInteractions(todoService);
    }

    private static Stream<Arguments> generateParamToGetOne() {
        return Stream.of(
                Arguments.of(1L, TODO_DTO_1),
                Arguments.of(2L, TODO_DTO_2));
    }

    @Test
    public void getOne_nothingFound_returns404() throws Exception {
        //given
        when(todoService.findById(3L)).thenThrow(new EntityNotFoundException());
        //when
        mockMvc.perform(get(URL + "/" + 3L))
                .andDo(print())
                //then
                .andExpect(jsonPath("$", is(String.format("Not found element with id: %s", 3L))))
                .andExpect(status().isNotFound());
        verify(todoService, times(1)).findById(3L);
        verifyNoMoreInteractions(todoService);
    }

    @Test
    public void post_elementWithoutName_returns400() throws Exception {
        //given
        Todo todo = Todo.builder()
                .priority("wazny")
                .description("opis")
                .completed(false)
                .build();
        //when
        mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJson(todo))
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().is4xxClientError());
        verifyNoMoreInteractions(todoService);
    }

    @Test
    public void post_completeElement_returns201() throws Exception {
        //given
        Todo todo = Todo.builder()
                .name("nazwa")
                .priority("priorytet")
                .description("opis")
                .completed(false)
                .user(USER_TEST_USER)
                .build();
        lenient().when(todoService.save(todo)).thenReturn(1L);
        //when
        mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJson(todo)))
                .andDo(print())
                //then
                .andExpect(jsonPath("$", is(1)))
                .andExpect(status().isCreated());
        verify(todoService, times(1)).save(todo);
        verifyNoMoreInteractions(todoService);
    }

    @Test
    public void put_elementWithoutName_returns400() throws Exception {
        //given
        Todo todo = Todo.builder()
                .priority("priorytet")
                .description("opis")
                .completed(true)
                .build();
        //when
        mockMvc.perform(put(URL)
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJson(todo)))
                .andDo(print())
                //then
                .andExpect(status().is4xxClientError());
        verify(todoService, never()).update(any(), anyLong());

    }

    @Test
    public void put_completeElementWhenTodoExists_returns2xx() throws Exception {
        //given
        Todo todo = Todo.builder()
                .name("nazwa")
                .priority("priorytet")
                .description("opis")
                .completed(false)
                .build();
        when(todoService.update(todo, 1L)).thenReturn("Todo updated!");
        //when
        mockMvc.perform(put(URL + "/1")
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJson(todo)))
                .andDo(print())
                //then
                .andExpect(status().is2xxSuccessful());
        verify(todoService, times(1)).update(todo, 1L);
        verifyNoMoreInteractions(todoService);
    }

    @Test
    public void put_completeElementWhenTodoNotExists_returns404() throws Exception {
        //given
        Todo todo = Todo.builder()
                .name("nazwa")
                .priority("priorytet")
                .description("opis")
                .completed(false)
                .build();
        when(todoService.update(todo, 1L)).thenThrow(new EntityNotFoundException("1"));
        //when
        mockMvc.perform(put(URL + "/1")
                        .contentType(APPLICATION_JSON)
                        .content(convertObjectToJson(todo))
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Not found element with id: " + 1L)));
        verify(todoService, times(1)).update(any(), anyLong());
        verify(todoService, never()).save(any());
        verify(todoService, never()).existsById(anyLong());
        verifyNoMoreInteractions(todoService);
    }

    @Test
    public void delete_TodoNotExists_returns404() throws Exception {
        //given

        when(todoService.delete(2L)).thenThrow(new EntityNotFoundException("2L"));
        //when
        mockMvc.perform(delete(URL + "/2"))
                .andDo(print())
                //then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Not found element with id: 2")));
        verify(todoService, times(1)).delete(2L);
        verifyNoMoreInteractions(todoService);
    }

    @Test
    public void delete_TodoExists_returns204() throws Exception {
        //given
        when(todoService.delete(1L)).thenReturn("Todo deleted");
        //when
        mockMvc.perform(delete(URL + "/1")
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isNoContent());
        verify(todoService, times(1)).delete(anyLong());
        verifyNoMoreInteractions(todoService);

    }
}
