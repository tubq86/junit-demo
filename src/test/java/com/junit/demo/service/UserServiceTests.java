package com.junit.demo.service;

import com.junit.demo.entity.User;
import com.junit.demo.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;

import com.junit.demo.service.impl.UserServiceImpl;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

/**
 * @author : TuBQ
 * @since : 7/3/2023, Mon
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void init() {
        user = User.builder()
                .id(1L)
                .name("Nam")
                .email("namlh@gmail.com")
                .build();
    }

    // JUnit test for saveUser method
    @DisplayName("JUnit test for saveUser method")
    @Test
    public void givenUserObject_whenSaveUser_thenReturnUserObject(){
        // given - precondition or setup
        given(userRepository.findByEmail(user.getEmail()))
                .willReturn(Optional.empty());

        given(userRepository.save(user)).willReturn(user);

        System.out.println(userRepository);
        System.out.println(userService);

        // when -  action or the behaviour that we are going test
        User savedUser = userService.addUser(user);

        System.out.println(savedUser);
        // then - verify the output
        assertThat(savedUser).isNotNull();
    }

    // JUnit test for saveUser method
    @DisplayName("JUnit test for saveUser method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveUser_thenThrowsException(){
        // given - precondition or setup
        given(userRepository.findByEmail(user.getEmail()))
                .willReturn(Optional.of(user));

        System.out.println(userRepository);
        System.out.println(userService);

        // when -  action or the behaviour that we are going test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.addUser(user);
        });

        // then
        verify(userRepository, never()).save(any(User.class));
    }

    // JUnit test for getAllUsers method
    @DisplayName("JUnit test for getAllUsers method")
    @Test
    public void givenUsersList_whenGetAllUsers_thenReturnUsersList(){
        // given - precondition or setup

        User user1 = User.builder()
                .id(2L)
                .name("Stark")
                .email("stark@gmail.com")
                .build();

        given(userRepository.findAll()).willReturn(Arrays.asList(user, user1));

        // when -  action or the behaviour that we are going test
        List<User> userList = userService.getAllUsers();

        // then - verify the output
        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);
    }

    // JUnit test for getAllUsers method
    @DisplayName("JUnit test for getAllUsers method (negative scenario)")
    @Test
    public void givenEmptyUsersList_whenGetAllUsers_thenReturnEmptyUsersList(){
        // given - precondition or setup

        User user1 = User.builder()
                .id(2L)
                .name("Stark")
                .email("stark@gmail.com")
                .build();

        given(userRepository.findAll()).willReturn(Collections.emptyList());

        // when -  action or the behaviour that we are going test
        List<User> userList = userService.getAllUsers();

        // then - verify the output
        assertThat(userList).isEmpty();
        assertThat(userList.size()).isEqualTo(0);
    }

    // JUnit test for getUserById method
    @DisplayName("JUnit test for getUserById method")
    @Test
    public void givenUserId_whenGetUserById_thenReturnUserObject(){
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when
        User savedUser = userService.getUserById(user.getId()).get();

        // then
        assertThat(savedUser).isNotNull();

    }

    // JUnit test for updateUser method
    @DisplayName("JUnit test for updateUser method")
    @Test
    public void givenUserObject_whenUpdateUser_thenReturnUpdatedUser(){
        // given - precondition or setup
        given(userRepository.save(user)).willReturn(user);
        user.setEmail("ram@gmail.com");
        user.setName("Ram");
        // when -  action or the behaviour that we are going test
        User updatedUser = userService.updateUser(user);

        // then - verify the output
        assertThat(updatedUser.getEmail()).isEqualTo("ram@gmail.com");
        assertThat(updatedUser.getName()).isEqualTo("Ram");
    }

    // JUnit test for deleteUser method
    @DisplayName("JUnit test for deleteUser method")
    @Test
    public void givenUserId_whenDeleteUser_thenNothing(){
        // given - precondition or setup
        long userId = 1L;

        willDoNothing().given(userRepository).deleteById(userId);

        // when -  action or the behaviour that we are going test
        userService.deleteUser(userId);

        // then - verify the output
        verify(userRepository, times(1)).deleteById(userId);
    }
}
