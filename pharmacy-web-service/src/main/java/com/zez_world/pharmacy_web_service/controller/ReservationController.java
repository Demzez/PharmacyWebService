package com.zez_world.pharmacy_web_service.controller;

import com.zez_world.pharmacy_web_service.dto.request.ReservationRequestDTO;
import com.zez_world.pharmacy_web_service.dto.response.ReservationResponseDTO;
import com.zez_world.pharmacy_web_service.service.ReservationService;
import com.zez_world.pharmacy_web_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequestDTO request) {
        try {
            Long userId = getCurrentUserId();
            ReservationResponseDTO reservation = reservationService.createReservation(userId, request);
            return ResponseEntity.ok(reservation);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<ReservationResponseDTO>> getUserReservations() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(reservationService.getUserReservations(userId));
    }

    @GetMapping("/user/active")
    public ResponseEntity<List<ReservationResponseDTO>> getActiveUserReservations() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(reservationService.getActiveUserReservations(userId));
    }

    @PutMapping("/{reservationId}/complete")
    public ResponseEntity<ReservationResponseDTO> completeReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.completeReservation(reservationId));
    }

    @DeleteMapping("/{reservationId}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable Long reservationId) {
        try {
            reservationService.cancelReservation(reservationId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Reservation cancelled successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.getUserIdByUsername(username);
    }
}