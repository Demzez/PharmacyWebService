package com.zez_world.pharmacy_web_service.controller;

import com.zez_world.pharmacy_web_service.dto.request.ReservationRequestDTO;
import com.zez_world.pharmacy_web_service.dto.response.ReservationResponseDTO;
import com.zez_world.pharmacy_web_service.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequestDTO request,
                                               @RequestParam Long userId) {
        try {
            ReservationResponseDTO reservation = reservationService.createReservation(userId, request);
            return ResponseEntity.ok(reservation);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponseDTO>> getUserReservations(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getUserReservations(userId));
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<ReservationResponseDTO>> getActiveUserReservations(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getActiveUserReservations(userId));
    }

    @PutMapping("/{reservationId}/complete")
    public ResponseEntity<ReservationResponseDTO> completeReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.completeReservation(reservationId));
    }

    @DeleteMapping("/{reservationId}")
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
}