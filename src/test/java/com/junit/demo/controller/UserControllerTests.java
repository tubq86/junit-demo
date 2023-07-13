package com.junit.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junit.demo.entity.User;
import com.junit.demo.service.UserService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * @author : TuBQ
 * @since : 7/4/2023, Tue
 */
@WebMvcTest
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenUserObject_whenCreateUser_thenReturnSavedUser() throws Exception{

        // given - precondition or setup
        User user = User.builder()
                .name("Nam")
                .email("namlh@gmail.com")
                .build();
        given(userService.addUser(any(User.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.name",
                        is(user.getName())))
                .andExpect(jsonPath("$.email",
                        is(user.getEmail())));

    }

    // JUnit test for Get All users REST API
    @Test
    public void givenListOfUsers_whenGetAllUsers_thenReturnUsersList() throws Exception{
        // given - precondition or setup
        List<User> listOfUsers = new ArrayList<>();
        listOfUsers.add(User.builder().name("Ramesh").email("namlh@gmail.com").build());
        listOfUsers.add(User.builder().name("Stark").email("stark@gmail.com").build());
        given(userService.getAllUsers()).willReturn(listOfUsers);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/users"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfUsers.size())));

    }

    // positive scenario - valid user id
    // JUnit test for GET user by id REST API
    @Test
    public void givenUserId_whenGetUserById_thenReturnUserObject() throws Exception{
        // given - precondition or setup
        long userId = 1L;
        User user = User.builder()
                .name("Nam")
                .email("namlh@gmail.com")
                .build();
        given(userService.getUserById(userId)).willReturn(Optional.of(user));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/users/{id}", userId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));

    }

    // negative scenario - valid user id
    // JUnit test for GET user by id REST API
    @Test
    public void givenInvalidUserId_whenGetUserById_thenReturnEmpty() throws Exception{
        // given - precondition or setup
        long userId = 1L;
        User user = User.builder()
                .name("Nam")
                .email("namlh@gmail.com")
                .build();
        given(userService.getUserById(userId)).willReturn(Optional.empty());

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/users/{id}", userId));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }
    // JUnit test for update user REST API - positive scenario
    @Test
    public void givenUpdatedUser_whenUpdateUser_thenReturnUpdateUserObject() throws Exception{
        // given - precondition or setup
        long userId = 1L;
        User savedUser = User.builder()
                .name("Nam")
                .email("namlh@gmail.com")
                .build();

        User updatedUser = User.builder()
                .name("Ram")
                .email("ram@gmail.com")
                .build();
        given(userService.getUserById(userId)).willReturn(Optional.of(savedUser));
        given(userService.updateUser(any(User.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)));


        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(updatedUser.getName())))
                .andExpect(jsonPath("$.email", is(updatedUser.getEmail())));
    }

    // JUnit test for update user REST API - negative scenario
    @Test
    public void givenUpdatedUser_whenUpdateUser_thenReturn404() throws Exception{
        // given - precondition or setup
        long userId = 1L;
        User savedUser = User.builder()
                .name("Nam")
                .email("namlh@gmail.com")
                .build();

        User updatedUser = User.builder()
                .name("Ram")
                .email("ram@gmail.com")
                .build();
        given(userService.getUserById(userId)).willReturn(Optional.empty());
        given(userService.updateUser(any(User.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // JUnit test for delete user REST API
    @Test
    public void givenUserId_whenDeleteUser_thenReturn200() throws Exception{
        // given - precondition or setup
        long userId = 1L;
        willDoNothing().given(userService).deleteUser(userId);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/users/{id}", userId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
