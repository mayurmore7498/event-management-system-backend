package com.example.eventmanagement.controller;

import com.example.eventmanagement.model.User;
import com.example.eventmanagement.security.JwtTokenProvider;
import com.example.eventmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/organizers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getAllOrganizers() {
        return userService.getUsersByRole("ORGANIZER");
    }


    
   
    @GetMapping("/me")
    public User getMyProfile(HttpServletRequest request) {
        String token = extractToken(request);
        String email = jwtTokenProvider.getEmailFromToken(token);
        return userService.getUserByEmail(email);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new RuntimeException("Missing or invalid Authorization header");
    }
}
