package com.zez_world.pharmacy_web_service.dto.response;

import com.zez_world.pharmacy_web_service.entity.PrescriptionStatus;
import com.zez_world.pharmacy_web_service.entity.Product;
import java.time.LocalDate;

public class ProductResponseDTO {
    private Long id;
    private String name;
    private String manufacturer;
    private String releaseForm;
    private LocalDate expiryDate;
    private PrescriptionStatus prescriptionStatus;
    private Double price;
    private Integer stockQuantity;
    private String activeSubstance;
    private String category;
    private boolean available;
    private boolean visible;

    // Для публичного каталога
    public static ProductResponseDTO fromPublic(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.id = product.getId();
        dto.name = product.getName();
        dto.manufacturer = product.getManufacturer();
        dto.releaseForm = product.getReleaseForm();
        dto.expiryDate = product.getExpiryDate();
        dto.prescriptionStatus = product.getPrescriptionStatus();
        dto.price = product.getPrice();
        dto.stockQuantity = product.getStockQuantity();
        dto.activeSubstance = product.getActiveSubstance();
        dto.category = product.getCategory();
        dto.available = product.getStockQuantity() > 0;
        dto.visible = product.isVisible();
        return dto;
    }

    // Для администратора (все поля)
    public static ProductResponseDTO fromAdmin(Product product) {
        ProductResponseDTO dto = fromPublic(product);
        // Можно добавить дополнительные поля для админа
        return dto;
    }

    // Геттеры
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getManufacturer() { return manufacturer; }
    public String getReleaseForm() { return releaseForm; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public PrescriptionStatus getPrescriptionStatus() { return prescriptionStatus; }
    public Double getPrice() { return price; }
    public Integer getStockQuantity() { return stockQuantity; }
    public String getActiveSubstance() { return activeSubstance; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return available; }
    public boolean isVisible() { return visible; }
}