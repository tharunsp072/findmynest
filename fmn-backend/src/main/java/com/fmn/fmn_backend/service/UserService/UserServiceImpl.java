package com.fmn.fmn_backend.service.UserService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmn.fmn_backend.entity.User;
import com.fmn.fmn_backend.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepo;
    @Override
    public Optional<User> findUserById(Long id) {
        return userRepo.findById(id);
    }
    
}
