package com.zez_world.pharmacy_web_service.repository;

import com.zez_world.pharmacy_web_service.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByProductId(Long productId);
    List<Reservation> findByCompletedFalseAndExpiryDateBefore(LocalDateTime date);
    List<Reservation> findByCompletedTrue();

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.completed = false")
    List<Reservation> findActiveReservationsByUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.product.id = :productId AND r.completed = false")
    Long countActiveReservationsByProduct(@Param("productId") Long productId);
}