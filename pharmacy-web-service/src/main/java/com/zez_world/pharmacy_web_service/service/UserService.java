package com.zez_world.pharmacy_web_service.service;

import com.zez_world.pharmacy_web_service.dto.request.UserCreateDTO;
import com.zez_world.pharmacy_web_service.dto.response.UserResponseDTO;
import com.zez_world.pharmacy_web_service.entity.Role;
import com.zez_world.pharmacy_web_service.entity.User;
import com.zez_world.pharmacy_web_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO registerUser(UserCreateDTO userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setRole(Role.ROLE_USER);

        User savedUser = userRepository.save(user);
        return UserResponseDTO.from(savedUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findByRole(Role.ROLE_USER)
                .stream()
                .map(UserResponseDTO::from)
                .collect(Collectors.toList());
    }

    public boolean validateUserCredentials(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Long getUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }
}