package com.zez_world.pharmacy_web_service.service;

import com.zez_world.pharmacy_web_service.dto.request.ReservationRequestDTO;
import com.zez_world.pharmacy_web_service.dto.response.ReservationResponseDTO;
import com.zez_world.pharmacy_web_service.entity.Product;
import com.zez_world.pharmacy_web_service.entity.Reservation;
import com.zez_world.pharmacy_web_service.entity.User;
import com.zez_world.pharmacy_web_service.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SalesService salesService;

    public ReservationResponseDTO createReservation(Long userId, ReservationRequestDTO reservationDto) {
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productService.getProductEntityById(reservationDto.getProductId());

        if (!product.isVisible()) {
            throw new RuntimeException("Product is not available");
        }

        if (product.getStockQuantity() < reservationDto.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        productService.updateStockQuantity(reservationDto.getProductId(), -reservationDto.getQuantity());

        Reservation reservation = new Reservation(user, product, reservationDto.getQuantity());
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponseDTO.from(savedReservation);
    }

    public List<ReservationResponseDTO> getUserReservations(Long userId) {
        return reservationRepository.findByUserId(userId)
                .stream()
                .map(ReservationResponseDTO::from)
                .collect(Collectors.toList());
    }

    public ReservationResponseDTO completeReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setCompleted(true);
        salesService.recordSale(reservation.getProduct().getId(), reservation.getQuantity(), reservation.getProduct().getPrice());
        Reservation updatedReservation = reservationRepository.save(reservation);
        return ReservationResponseDTO.from(updatedReservation);
    }

    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        productService.updateStockQuantity(reservation.getProduct().getId(), reservation.getQuantity());

        reservationRepository.delete(reservation);
    }

    public int countActiveReservations() {
        return reservationRepository.countByCompletedFalse();
    }

    // отмена просроченных и чистка выполненных бронирований
    @Scheduled(fixedRate = 3600000) // каждый час
    public void cancelExpiredReservations() {
        List<Reservation> expiredReservations =
                reservationRepository.findByCompletedFalseAndExpiryDateBefore(LocalDateTime.now());

        for (Reservation reservation : expiredReservations) {
            cancelReservation(reservation.getId());
        }

        for (Reservation reservation : reservationRepository.findByCompletedTrue()) {
            reservationRepository.delete(reservation);
        }
    }

    public Optional<ReservationResponseDTO> getReservationById(Long id) {
        return reservationRepository.findById(id)
                .map(ReservationResponseDTO::from);
    }
}