package com.zez_world.pharmacy_web_service.controller;

import com.zez_world.pharmacy_web_service.dto.request.LoginRequestDTO;
import com.zez_world.pharmacy_web_service.dto.request.UserCreateDTO;
import com.zez_world.pharmacy_web_service.dto.response.UserResponseDTO;
import com.zez_world.pharmacy_web_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserCreateDTO userDto) {
        try {
            UserResponseDTO registeredUser = userService.registerUser(userDto);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        boolean isValid = userService.validateUserCredentials(
                loginRequest.getUsername(), loginRequest.getPassword());

        if (isValid) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("username", loginRequest.getUsername());
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Invalid credentials");
            return ResponseEntity.status(401).body(response);
        }
    }
}