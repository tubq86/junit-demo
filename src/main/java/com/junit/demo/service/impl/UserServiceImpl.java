package com.junit.demo.service.impl;

import com.junit.demo.entity.User;
import com.junit.demo.repository.UserRepository;
import com.junit.demo.service.UserService;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author : TuBQ
 * @since : 7/3/2023, Mon
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User addUser(User user) {
        Optional<User> savedUser = userRepository.findByEmail(user.getEmail());
        if(savedUser.isPresent()){
            throw new ResourceNotFoundException("Employee already exist with given email:" + user.getEmail());
        }

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateUser(User updatedUser) {
        return userRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
