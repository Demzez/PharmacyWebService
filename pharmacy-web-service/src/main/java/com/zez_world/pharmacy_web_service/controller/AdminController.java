package com.zez_world.pharmacy_web_service.controller;

import com.zez_world.pharmacy_web_service.dto.request.ProductCreateDTO;
import com.zez_world.pharmacy_web_service.dto.request.UserCreateDTO;
import com.zez_world.pharmacy_web_service.dto.response.ProductResponseDTO;
import com.zez_world.pharmacy_web_service.dto.response.UserResponseDTO;
import com.zez_world.pharmacy_web_service.entity.Role;
import com.zez_world.pharmacy_web_service.service.AdminService;
import com.zez_world.pharmacy_web_service.service.ProductService;
import com.zez_world.pharmacy_web_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // Управление пользователями
    @PostMapping("/users/create")
    public ResponseEntity<UserResponseDTO> createAdmin(@RequestBody UserCreateDTO userDto) {
        return ResponseEntity.ok(userService.createAdmin(userDto));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserCreateDTO userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }

    // Управление продуктами
    @PostMapping("/products")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductCreateDTO productDto) {
        return ResponseEntity.ok(productService.createProduct(productDto));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductCreateDTO productDto) {
        return ResponseEntity.ok(productService.updateProduct(id, productDto));
    }

    @PutMapping("/products/{id}/visibility")
    public ResponseEntity<ProductResponseDTO> toggleProductVisibility(@PathVariable Long id) {
        return ResponseEntity.ok(productService.toggleProductVisibility(id));
    }

    // Отчеты
    @GetMapping("/reports/sales")
    public ResponseEntity<Map<String, Object>> getSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(adminService.getSalesReport(startDate, endDate));
    }

    @GetMapping("/reports/popular")
    public ResponseEntity<Map<String, Object>> getPopularProducts() {
        return ResponseEntity.ok(adminService.getPopularProducts());
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getSystemStatistics() {
        return ResponseEntity.ok(adminService.getSystemStatistics());
    }
}