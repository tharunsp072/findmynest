package com.fmn.fmn_backend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fmn.fmn_backend.entity.User;
import com.fmn.fmn_backend.service.UserService.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id){
        return userService.findUserById(id);
    }
}
