package com.inetum.training.todo.controller;

import com.inetum.training.todo.domain.Todo;
import com.inetum.training.todo.service.fake.TodoFakeRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


public class StandaloneTodoRestControllerTest {

    private MockMvc mockMvc;

    private TodoFakeRepositoryImpl fakeRepository;

    public static final String URL = "/todos";

    @BeforeEach
    public void setUp() {
        fakeRepository = new TodoFakeRepositoryImpl();
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
    }

    @Test
    public void getAll_TodoFound_returnsTodo() throws Exception {
        //given
        Todo todo = new Todo(1L, "nazwa", "priorytet", "opis", true);
        Todo todo2 = new Todo(2L, "nazwa2", "priorytet2", "opis2", true);
        fakeRepository.save(todo);
        fakeRepository.save(todo2);
        //when
        mockMvc.perform(get(URL))
                .andDo(print())
                //then
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("nazwa")))
                .andExpect(jsonPath("$[1].name", is("nazwa2")));
    }

    @Test
    public void getOne_nothingFound_returns404() throws Exception {
        //when
        mockMvc.perform(get(URL + "/1"))
                .andDo(print())
                //then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("nie znaleziono elementu o podanym id")));
    }

    @Test
    public void getOne_TodoFound_returns200() throws Exception {
        //given
        Todo todo = new Todo(1L, "nazwa", "priorytet", "opis", true);
        fakeRepository.save(todo);
        //when
        mockMvc.perform(get(URL + "/1")
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

    }

    @Test
    public void post_completeElement_returns201() throws Exception {
        //given
        Todo todo = new Todo(1L, "nazwa", "priorytet", "opis", true);
        Todo todo2 = new Todo(2L, "nazwa2", "priorytet2", "opis2", true);
        fakeRepository.save(todo);
        fakeRepository.save(todo2);
        String jsonTodo = "{\"name\":\"nowa nazwa\", " +
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
                .andExpect(status().isCreated());

    }

    @Test
    public void put_elementWithoutName_returns400() throws Exception {
        //given
        String jsonTodo = "{\"id\":\"2\", " +
                "\"priority\":\"wazny\", " +
                "\"description\":\"opis\", " +
                "\"completed\":\"false\"}";
        //when
        mockMvc.perform(put(URL + "/2")
                        .contentType(APPLICATION_JSON)
                        .content(jsonTodo)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void put_completeElementWhenTodoExists_returns2xx() throws Exception {
        //given
        Todo todo = new Todo(2L, "nazwa1", "priorytet3", "opis1", true);
        fakeRepository.save(todo);
        String jsonTodo = "{\"name\":\"nazwa\", " +
                "\"priority\":\"wazny\", " +
                "\"description\":\"opis\", " +
                "\"completed\":\"false\"}";
        //when
        mockMvc.perform(put(URL + "/2")
                        .contentType(APPLICATION_JSON)
                        .content(jsonTodo)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void put_completeElementWhenTodoNotExists_returns2xx() throws Exception {
        //given
        Todo todo = new Todo(1L, "nazwa1", "priorytet3", "opis1", true);
        fakeRepository.save(todo);
        String jsonTodo = "{\"id\":\"2\", " +
                "\"name\":\"nazwa\", " +
                "\"priority\":\"wazny\", " +
                "\"description\":\"opis\", " +
                "\"completed\":\"false\"}";
        //when
        mockMvc.perform(put(URL + "/2")
                        .contentType(APPLICATION_JSON)
                        .content(jsonTodo)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isOk());
    }

    @Test
    public void put_withoutNameElementWhenTodoNotExists_returns4xx() throws Exception {
        //given
        Todo todo = new Todo(1L, "nazwa1", "priorytet3", "opis1", true);
        fakeRepository.save(todo);
        String jsonTodo = "{\"id\":\"2\", " +
                "\"priority\":\"wazny\", " +
                "\"description\":\"opis\", " +
                "\"completed\":\"false\"}";
        //when
        mockMvc.perform(put(URL + "/2")
                        .contentType(APPLICATION_JSON)
                        .content(jsonTodo)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void delete_TodoExists_returns204() throws Exception {
        //given
        Todo todo = new Todo(1L, "nazwa1", "priorytet3", "opis1", true);
        fakeRepository.save(todo);
        //when
        mockMvc.perform(delete(URL + "/1")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().isNoContent());
    }

    @Test
    public void delete_TodoNotExists_returns404() throws Exception {
        //given
        Todo todo = new Todo(2L, "nazwa1", "priorytet3", "opis1", true);
        fakeRepository.save(todo);
        //when
        mockMvc.perform(delete(URL + "/100")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                //then
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$", is("nie znaleziono elementu o podanym id")));
    }


}
