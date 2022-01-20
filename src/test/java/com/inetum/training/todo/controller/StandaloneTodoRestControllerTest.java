package com.inetum.training.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.service.fake.TodoFakeRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
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
    private TodoFakeRepositoryImpl fakeRepository;

    private MockMvc mockMvc;
    public static final String URL = "/todos";

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    public void setUp() {
        mockMvc = standaloneSetup(new TodoRestController(fakeRepository))
                .build();
    }

    @Test
    public void getAll_properRequest_returns200() throws Exception {

        //given
        MockHttpServletRequestBuilder mockRequest = get(URL);
        //when
        mockMvc.perform(mockRequest)
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
        verify(fakeRepository, times(1)).findAll();
    }

    @Test
    public void getAll_TodoFound_returnsTodo() throws Exception {
        //given
        Todo todo1 = new Todo(1L, "nazwa1", "priorytet1", "opis1", true);
        Todo todo2 = new Todo(2L, "nazwa2", "priorytet2", "opis2", true);
        List<Todo> listTodo = Arrays.asList(todo1, todo2);

        when(fakeRepository.findAll())
                .thenReturn(newArrayList(listTodo));
        //when
        mockMvc.perform(get(URL)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("nazwa1")))
                .andExpect(jsonPath("$[1].name", is("nazwa2")));
    }

    @Test
    public void getOne_nothingFound_returns404() throws Exception {
        //given
        when(fakeRepository.findById(3L))
                .thenThrow(EntityNotFoundException.class);
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
                .andExpect(jsonPath("$.id").value(1));
    }

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
        verify(fakeRepository, times(0)).save(any());

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
        verify(fakeRepository, times(0)).save(any());
        verify(fakeRepository, times(0)).existsById(any());

    }

    @Test
    public void put_completeElementWhenTodoExists_returns2xx() throws Exception {
        //given
        String jsonTodo = "{\"name\":\"nazwa\", " +
                "\"priority\":\"wazny\", " +
                "\"description\":\"opis\", " +
                "\"completed\":\"false\"}";
        Todo todoFromJson = objectMapper.readValue(jsonTodo, Todo.class);
        todoFromJson.setId(2L);
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
        verify(fakeRepository, times(1)).save(any());
        verify(fakeRepository, times(1)).existsById(any());

    }

    @Test
    public void put_completeElementWhenTodoNotExists_returns404() throws Exception {
        //given
        String jsonTodo = "{\"name\":\"nazwa\", " +
                "\"priority\":\"wazny\", " +
                "\"description\":\"opis\", " +
                "\"completed\":\"false\"}";
        when(fakeRepository.existsById(2L)).thenThrow(EntityNotFoundException.class);
        //when
        mockMvc.perform(put(URL + "/2")
                        .contentType(APPLICATION_JSON)
                        .content(jsonTodo)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("nie znaleziono elementu o podanym id")));
        verify(fakeRepository, times(0)).save(any());
        verify(fakeRepository, times(1)).existsById(any());
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
        verify(fakeRepository, times(1)).existsById(any());
        verify(fakeRepository, times(1)).deleteById(any());

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
        verify(fakeRepository, times(1)).existsById(any());
        verify(fakeRepository, times(0)).deleteById(any());
    }


}
