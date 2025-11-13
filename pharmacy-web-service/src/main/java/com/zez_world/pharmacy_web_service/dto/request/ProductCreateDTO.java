package com.zez_world.pharmacy_web_service.dto.request;

import com.zez_world.pharmacy_web_service.entity.PrescriptionStatus;
import java.time.LocalDate;

public class ProductCreateDTO {
    private String name;
    private String manufacturer;
    private String releaseForm;
    private LocalDate expiryDate;
    private PrescriptionStatus prescriptionStatus;
    private Double price;
    private Integer stockQuantity;
    private String activeSubstance;
    private String category;

    public ProductCreateDTO() {}

    // Геттеры и сеттеры
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getReleaseForm() { return releaseForm; }
    public void setReleaseForm(String releaseForm) { this.releaseForm = releaseForm; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public PrescriptionStatus getPrescriptionStatus() { return prescriptionStatus; }
    public void setPrescriptionStatus(PrescriptionStatus prescriptionStatus) { this.prescriptionStatus = prescriptionStatus; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    public String getActiveSubstance() { return activeSubstance; }
    public void setActiveSubstance(String activeSubstance) { this.activeSubstance = activeSubstance; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
