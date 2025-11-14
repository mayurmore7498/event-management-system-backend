package com.example.eventmanagement.service;

import com.example.eventmanagement.model.User;
import com.example.eventmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

  
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(User.Role.valueOf(role));
    }


}
