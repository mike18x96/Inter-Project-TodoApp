//package com.inetum.training.user.controller;
//
//import com.inetum.training.TodoAppApplication;
//import com.inetum.training.entity.user.model.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import static com.inetum.training.TestJsonUtils.convertObjectToJson;
//import static com.inetum.training.entity.user.model.User.Role.USER;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ContextConfiguration(classes = {TodoAppApplication.class})
//@WebAppConfiguration
//@SpringBootTest
//public class UserControllerSecurityTest {
//
//    @Autowired
//    private WebApplicationContext context;
//
//    private static final String URL = "/users";
//
//    private final User USER_TEST = new User(1L, "login", "password", USER);
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
//    }
//
//    @Test
//    public void getAll_authorizedAdminUser_returns200() throws Exception {
//        //when
//        mockMvc.perform(get(URL).with(user("admin").roles("ADMIN")))
//                //then
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void getAll_noCredentials_returns401() throws Exception {
//        //when
//        mockMvc.perform(get(URL).with(anonymous()))
//                //then
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void getAll_authorizedPlainUser_returns403() throws Exception {
//        //when
//        mockMvc.perform(get(URL).with(user("user").roles("USER")))
//                //then
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void create_authorizedByAdmin_returns200() throws Exception {
//        //when
//        mockMvc.perform(post(URL).with(user("admin").roles("ADMIN"))
//                        .contentType(APPLICATION_JSON)
//                        .content(convertObjectToJson(USER_TEST)))
//                .andDo(print())
//                //then
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    public void create_noCredentials_returns401() throws Exception {
//        //when
//        mockMvc.perform(post(URL).with(anonymous()))
//                //then
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void create_authorizedByUser_returns403() throws Exception {
//        //given
//        //when
//        mockMvc.perform(post(URL).with(user("henio").roles("USER"))
//                        .contentType(APPLICATION_JSON)
//                        .content(convertObjectToJson(USER_TEST)))
//                .andDo(print())
//                //then
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void updatePasswordByAdmin_authorizedByAdmin_returns200() throws Exception {
//        //when
//        mockMvc.perform(put(URL + "/resetPasswordByAdmin" + "/" + USER_TEST.getId().toString())
//                        .with(user("admin").roles("ADMIN"))
//                        .contentType(APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void updatePasswordByAdmin_noCredentials_returns401() throws Exception {
//        //when
//        mockMvc.perform(put(URL + "/resetPasswordByAdmin")
//                        .with(anonymous()))
//                //then
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void updatePasswordByAdmin_authorizedByUser_returns403() throws Exception {
//        //when
//        mockMvc.perform(put(URL + "/resetPasswordByAdmin" + "/" + USER_TEST.getId().toString())
//                        .with(user("henio").roles("USER"))
//                        .contentType(APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void updatePasswordByUser_authorizedByUser_returns200() throws Exception {
//        //when
//        mockMvc.perform(put(URL + "/resetUserPasswordByUser" + "/henio")
//                        .with(user("henio").roles("USER"))
//                        .contentType(APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void updatePasswordByUser_noCredentials_returns401() throws Exception {
//        //when
//        mockMvc.perform(put(URL + "/resetUserPasswordByUser")
//                        .with(anonymous()))
//                //then
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void updatePasswordByUser_authorizedByAdmin_returns403() throws Exception {
//        //when
//        mockMvc.perform(put(URL + "/resetUserPasswordByUser" + "/henio")
//                        .with(user("admin").roles("ADMIN"))
//                        .contentType(APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void updateUserRole_authorizedByAdmin_returns200() throws Exception {
//        //when
//        mockMvc.perform(put(URL + "/updateRoleByAdmin" + "/" + 1l + "/USER")
//                        .with(user("admin").roles("ADMIN"))
//                        .contentType(APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void updateUserRole_noCredentials_returns401() throws Exception {
//        //when
//        mockMvc.perform(put(URL + "/updateRoleByAdmin" + "/" + 1l + "/USER")
//                        .with(anonymous())
//                        .contentType(APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void updateUserRole_authorizedByUser_returns403() throws Exception {
//        //when
//        mockMvc.perform(put(URL + "/updateRoleByAdmin" + "/" + 1l + "/ADMIN")
//                        .with(user("henio").roles("USER"))
//                        .contentType(APPLICATION_JSON))
//                .andDo(print())
//                //then
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void delete_authorizedByAdmin_returns200() throws Exception {
//        //when
//        mockMvc.perform(delete(URL + "/1")
//                        .with(user("admin").roles("ADMIN")))
//                //then
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    public void delete_noCredentials_returns401() throws Exception {
//        //when
//        mockMvc.perform(delete(URL + "/1").with(anonymous()))
//                //then
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void delete_authorizedByUser_returns403() throws Exception {
//        //when
//        mockMvc.perform(delete(URL + "/1")
//                        .with(user("henio").roles("USER")))
//                //then
//                .andExpect(status().isForbidden());
//    }
//
//
//}
