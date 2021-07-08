package com.inetum.training.todo.controller;

import com.inetum.training.todo.service.fake.TodoFakeRepositoryImpl;
import com.inetum.training.todo.domain.Todo;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


public class StandaloneTodoRestControllerTest {

    private MockMvc mockMvc;

    private TodoFakeRepositoryImpl fakeRepository;

    public static final String URL = "/todos";

    @BeforeEach
    public void setUp(){
        fakeRepository = new TodoFakeRepositoryImpl();
        mockMvc = MockMvcBuilders.standaloneSetup(new TodoRestController(fakeRepository))
                 .build();
    }

    @Test
    public void getAll_properRequest_returns200() throws Exception {
        //given
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get(URL);
        MediaType applicationtJson= new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype()
        );
        //when
        mockMvc.perform(mockRequest)
        //then
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(applicationtJson));
    }

    @Test
    public void getAll_TodoFound_returnsTodo() throws Exception {
        //given
        Todo todo = new Todo(-1L, "nazwa", "priorytet", "opis", true);
        Todo todo2 = new Todo(-1L, "nazwa2", "priorytet2", "opis2", true);
        fakeRepository.save(todo);
        fakeRepository.save(todo2);
        //when
        mockMvc.perform(MockMvcRequestBuilders.get(URL))
//                .andDo(MockMvcResultHandlers.print())
        //then
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("nazwa")))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is("nazwa2")));
    }

    @Test
    public void getOne_nothingFound_returns404() throws Exception{
        //when
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/1"))
        //then
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void post_elementWithoutName_returns400() throws Exception{
        //given
        String jsonTodo = "{\"priority\":\"wazny\", " +
                           "\"description\":\"opis\", " +
                           "\"completed\":\"false\"}";
        MediaType applicationtJson = new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype()
        );
        //when
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                    .contentType(applicationtJson)
                    .content(jsonTodo)
        //then
        ).andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }


}
