package com.zez_world.pharmacy_web_service.controller;

import com.zez_world.pharmacy_web_service.dto.request.ProductCreateDTO;
import com.zez_world.pharmacy_web_service.dto.response.ProductResponseDTO;
import com.zez_world.pharmacy_web_service.dto.response.ReservationResponseDTO;
import com.zez_world.pharmacy_web_service.dto.response.reports.PopularDTO;
import com.zez_world.pharmacy_web_service.dto.response.reports.StatisticsDTO;
import com.zez_world.pharmacy_web_service.service.AdminService;
import com.zez_world.pharmacy_web_service.service.ProductService;
import com.zez_world.pharmacy_web_service.service.ReservationService;
import com.zez_world.pharmacy_web_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // Управление пользователями
//    @PostMapping("/users/create")
//    public ResponseEntity<UserResponseDTO> createAdmin(@RequestBody UserCreateDTO userDto) {
//        return ResponseEntity.ok(userService.createAdmin(userDto));
//    }
//
//    @GetMapping("/users")
//    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
//        return ResponseEntity.ok(userService.getAllUsers());
//    }
//
//
//    @PutMapping("/users/{id}")
//    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserCreateDTO userDto) {
//        return ResponseEntity.ok(userService.updateUser(id, userDto));
//    }
//
//    @DeleteMapping("/users/{id}")
//    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
//        userService.deactivateUser(id);
//        return ResponseEntity.ok().build();
//    }

    // Управление продуктами
    @GetMapping("/products/all_catalog")
    public ResponseEntity<List<ProductResponseDTO>> getCatalog() {
        return ResponseEntity.ok(productService.getCatalog());
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<ProductResponseDTO>> searchAdminProducts(@RequestParam String query) {
        return ResponseEntity.ok(productService.searchAdminProducts(query));
    }

    @PostMapping("/products/create")
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

    // Управление бронированиями
    @GetMapping("/user/reservations/{userLogin}")
    public ResponseEntity<List<ReservationResponseDTO>> getUserReservations(@PathVariable String userLogin) {
        return ResponseEntity.ok(reservationService.getUserReservations(userService.getUserIdByUsername(userLogin)));
    }

    @GetMapping("/reports/popular")
    public ResponseEntity<List<PopularDTO>> getPopularProducts() {
        List<PopularDTO> popularProducts = adminService.getPopularProducts();
        return ResponseEntity.ok(popularProducts);
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsDTO> getSystemStatistics() {
        StatisticsDTO statistics = adminService.getSystemStatistics();
        return ResponseEntity.ok(statistics);
    }
}