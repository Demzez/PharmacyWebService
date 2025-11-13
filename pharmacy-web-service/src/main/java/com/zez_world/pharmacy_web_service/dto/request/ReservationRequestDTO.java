package com.zez_world.pharmacy_web_service.dto.request;

public class ReservationRequestDTO {
    private Long productId;
    private Integer quantity;

    public ReservationRequestDTO() {}

    public ReservationRequestDTO(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Геттеры и сеттеры
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
