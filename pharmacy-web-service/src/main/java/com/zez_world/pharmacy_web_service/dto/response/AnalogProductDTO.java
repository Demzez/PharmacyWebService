package com.zez_world.pharmacy_web_service.dto.response;

public class AnalogProductDTO {
    private Long id;
    private String name;
    private String manufacturer;
    private Double price;
    private String activeSubstance;
    private Double priceDifference;

    public static AnalogProductDTO from(ProductResponseDTO product, ProductResponseDTO original) {
        AnalogProductDTO dto = new AnalogProductDTO();
        dto.id = product.getId();
        dto.name = product.getName();
        dto.manufacturer = product.getManufacturer();
        dto.price = product.getPrice();
        dto.activeSubstance = product.getActiveSubstance();
        dto.priceDifference = product.getPrice() - original.getPrice();
        return dto;
    }

    // Геттеры
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getManufacturer() { return manufacturer; }
    public Double getPrice() { return price; }
    public String getActiveSubstance() { return activeSubstance; }
    public Double getPriceDifference() { return priceDifference; }
}