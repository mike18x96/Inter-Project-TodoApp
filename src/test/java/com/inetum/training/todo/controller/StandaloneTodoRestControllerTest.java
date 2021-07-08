package com.inetum.training.todo.controller;

import com.inetum.training.todo.TestJsonUtils;
import com.inetum.training.todo.persistance.TodoJpaRepository;
import com.inetum.training.todo.domain.Todo;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StandaloneTodoRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TodoJpaRepository fakeRepository;

    public static final String URL = "/todos";

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(new TodoRestController(fakeRepository))
                 .build();
    }

    @Test
    public void getAll_properRequest_returns200() throws Exception {
        //given
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get(URL);
        when(fakeRepository.findAll()).thenReturn(emptyList());
        //when
        mockMvc.perform(mockRequest)
        //then
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(TestJsonUtils.APPLICATION_JSON));
    }

    @Test
    public void getAll_TodoFound_returnsTodo() throws Exception {
        //given
        Todo todo = new Todo(-1L, "nazwa", "priorytet", "opis", true);
        Todo todo2 = new Todo(-1L, "nazwa2", "priorytet2", "opis2", true);
        when(fakeRepository.findAll()).thenReturn(newArrayList(todo, todo2));
        //when
        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
        //then
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("nazwa")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is("nazwa2")));
    }

    @Test
    public void getOne_nothingFound_returns404() throws Exception{
        //given
        when(fakeRepository.findById(1L)).thenReturn(Optional.empty());
        //when
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/1"))
        //then
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void post_elementWithoutName_returns400() throws Exception{
        //given
        Todo todo = Todo.builder()
                .priority("wazny")
                .description("opis")
                .completed(false)
                .build();
        //when
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .contentType(TestJsonUtils.APPLICATION_JSON)
                .content(TestJsonUtils.convertObjectToJson(todo)))
                //then
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }


}
