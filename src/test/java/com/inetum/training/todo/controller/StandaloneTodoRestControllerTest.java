package com.inetum.training.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inetum.training.TestJsonUtils;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.persistance.TodoJpaRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)

public class StandaloneTodoRestControllerTest {

    @Mock
    private TodoJpaRepository fakeRepository;

    private MockMvc mockMvc;
    public static final String URL = "/todos";

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    public void setUp() {
        mockMvc = standaloneSetup(new TodoRestController(fakeRepository))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    public void getAll_properRequest_returns200() throws Exception {
        //given

        //when
        mockMvc.perform(get("/todos?page=0&size=2")
                .contentType(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isOk());
    }

    @Test
    public void getAll_TodoFound_returnsTodo() throws Exception {
        //given
        Todo todo1 = new Todo(1L, "nazwa1", "priorytet1", "opis1", true);
        Todo todo2 = new Todo(2L, "nazwa2", "priorytet2", "opis2", true);
        Todo todo3 = new Todo(3L, "nazwa3", "priorytet3", "opis3", true);
        List<Todo> listTodo = Arrays.asList(todo1, todo2, todo3);

        Pageable pageable = PageRequest.of(0, 2);
        PageImpl<Todo> todoPage = new PageImpl<>(listTodo, pageable, listTodo.size());

        when(fakeRepository.findAll(any(Pageable.class))).thenReturn(todoPage);

        //when
        mockMvc.perform(get("/todos?page=0&size=2")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(2)).andReturn();



//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].name", is("nazwa1")))
//                .andExpect(jsonPath("$[1].name", is("nazwa2")));
    }

    @Test
    public void getOne_nothingFound_returns404() throws Exception {
        //given
        when(fakeRepository.findById(3L))
                .thenReturn(Optional.empty());
        //when
        mockMvc.perform(get(URL + "/3"))
                .andDo(print())
                //then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("nie znaleziono elementu o podanym id")));
    }

    @Test
    public void getOne_TodoFound_returns200() throws Exception {
        //given
        Todo todo1 = new Todo(1L, "nazwa1", "priorytet1", "opis1", true);
        when(fakeRepository.findById(1L))
                .thenReturn(Optional.of(todo1));
        //when
        mockMvc.perform(get(URL + "/1")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("nazwa1")))
                .andExpect(jsonPath("$.priority", Matchers.is("priorytet1")))
                .andExpect(content().contentType(APPLICATION_JSON));
        verify(fakeRepository, times(1)).findById(anyLong());
    }

    @Disabled
    @Test
    public void post_elementWithoutName_returns400() throws Exception {
        //given
        String jsonTodo = "{\"priority\":\"wazny\", " +
                "\"description\":\"opis\", " +
                "\"completed\":\"false\"}";
        //when
        mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(jsonTodo)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().is4xxClientError());
        verify(fakeRepository, never()).save(any());
    }

    @Test
    public void post_elementWithoutName_returns400_v2() throws Exception{
        //given
        Todo todo = Todo.builder()
                .priority("wazny")
                .description("opis")
                .completed(false)
                .build();
        //when
        mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(TestJsonUtils.convertObjectToJson(todo)))
                //then
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void post_completeElement_returns201() throws Exception {
        //given

        String jsonTodo = "{\"name\":\"nowa nazwa\", " +
                "\"priority\":\"wazny\", " +
                "\"description\":\"opis\", " +
                "\"completed\":\"false\"}";
        Todo TodoFromJson = objectMapper.readValue(jsonTodo, Todo.class);

        when(fakeRepository.save(TodoFromJson))
                .thenReturn(TodoFromJson);
        //when
        mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(jsonTodo)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isCreated());
        verify(fakeRepository, times(1)).save(TodoFromJson);

    }

    @Test
    public void put_elementWithoutName_returns400() throws Exception {
        //given
        String jsonTodo = "{\"id\":\"2\", " +
                "\"priority\":\"wazny\", " +
                "\"description\":\"opis\", " +
                "\"completed\":\"false\"}";
        //when
        mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(jsonTodo)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().is4xxClientError());
        verify(fakeRepository, never()).save(any());
        verify(fakeRepository, never()).existsById(anyLong());

    }

    @Test
    public void put_completeElementWhenTodoExists_returns2xx() throws Exception {
        //given
        String jsonTodo = "{\"id\":\"2\", " +
                "\"name\":\"nazwa\", " +
                "\"priority\":\"wazny\", " +
                "\"description\":\"opis\", " +
                "\"completed\":\"false\"}";
        Todo todoFromJson = objectMapper.readValue(jsonTodo, Todo.class);
        when(fakeRepository.existsById(2L)).thenReturn(true);
        when(fakeRepository.save(todoFromJson)).thenReturn(todoFromJson);
        //when
        mockMvc.perform(put(URL + "/2")
                        .contentType(APPLICATION_JSON)
                        .content(jsonTodo)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().is2xxSuccessful());
        verify(fakeRepository, times(1)).save(todoFromJson);
        verify(fakeRepository, times(1)).existsById(anyLong());
    }

    @Test
    public void put_completeElementWhenTodoNotExists_returns404() throws Exception {
        //given
        String jsonTodo = "{\"name\":\"nazwa\", " +
                "\"priority\":\"wazny\", " +
                "\"description\":\"opis\", " +
                "\"completed\":\"false\"}";
        when(fakeRepository.existsById(2L)).thenReturn(false);
        //when
        mockMvc.perform(put(URL + "/2")
                        .contentType(APPLICATION_JSON)
                        .content(jsonTodo)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("nie znaleziono elementu o podanym id")));
        verify(fakeRepository, never()).save(any());
        verify(fakeRepository, times(1)).existsById(anyLong());
    }

    @Test
    public void delete_TodoExists_returns204() throws Exception {
        //given
        when(fakeRepository.existsById(1L)).thenReturn(true);
        //when
        mockMvc.perform(delete(URL + "/1")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isNoContent());
        verify(fakeRepository, times(1)).existsById(anyLong());
        verify(fakeRepository, times(1)).deleteById(anyLong());

    }

    @Test
    public void delete_TodoNotExists_returns404() throws Exception {
        //given
        when(fakeRepository.existsById(1L)).thenReturn(false);
        //when
        mockMvc.perform(delete(URL + "/1")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("nie znaleziono elementu o podanym id")));
        verify(fakeRepository, times(1)).existsById(anyLong());
        verify(fakeRepository, never()).deleteById(anyLong());
    }


}
