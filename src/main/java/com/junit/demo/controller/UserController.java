package com.junit.demo.controller;

import com.junit.demo.entity.User;
import com.junit.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : TuBQ
 * @since : 7/4/2023, Tue
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @GetMapping
    public List<User> getAllEmployees(){
        return userService.getAllUsers();
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getEmployeeById(@PathVariable("id") long userId){
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") long userId,
            @RequestBody User user){
        return userService.getUserById(userId)
                .map(usr -> {

                    usr.setName(user.getName());
                    usr.setEmail(user.getEmail());

                    User updatedUser = userService.updateUser(usr);
                    return new ResponseEntity<>(updatedUser, HttpStatus.OK);

                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") long userId){

        userService.deleteUser(userId);

        return new ResponseEntity<String>("User deleted successfully!.", HttpStatus.OK);

    }
}
