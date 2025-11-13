package com.zez_world.pharmacy_web_service.dto.response;

import com.zez_world.pharmacy_web_service.entity.Reservation;
import java.time.LocalDateTime;

public class ReservationResponseDTO {
    private Long id;
    private Long userId;
    private String username;
    private Long productId;
    private String productName;
    private Integer quantity;
    private LocalDateTime reservationDate;
    private LocalDateTime expiryDate;
    private boolean completed;
    private String status;

    public static ReservationResponseDTO from(Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.id = reservation.getId();
        dto.userId = reservation.getUser().getId();
        dto.username = reservation.getUser().getUsername();
        dto.productId = reservation.getProduct().getId();
        dto.productName = reservation.getProduct().getName();
        dto.quantity = reservation.getQuantity();
        dto.reservationDate = reservation.getReservationDate();
        dto.expiryDate = reservation.getExpiryDate();
        dto.completed = reservation.isCompleted();
        dto.status = reservation.isCompleted() ? "COMPLETED" :
                LocalDateTime.now().isAfter(reservation.getExpiryDate()) ? "EXPIRED" : "ACTIVE";
        return dto;
    }

    // Геттеры
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public Integer getQuantity() { return quantity; }
    public LocalDateTime getReservationDate() { return reservationDate; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public boolean isCompleted() { return completed; }
    public String getStatus() { return status; }
}
