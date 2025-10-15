package com.fmn.fmn_backend.service.UserService;

import java.util.List;
import java.util.Optional;

import com.fmn.fmn_backend.entity.User;

public interface UserService {
    Optional<User> findUserById(Long id);
    
} 
