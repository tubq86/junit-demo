package com.junit.demo.service;

import com.junit.demo.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * @author : TuBQ
 * @since : 7/3/2023, Mon
 */
public interface UserService {
    User addUser(User employee);
    List<User> getAllUsers();
    Optional<User> getUserById(long id);
    User updateUser(User updatedUser);
    void deleteUser(long id);
}
