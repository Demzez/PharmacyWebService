package com.zez_world.pharmacy_web_service.dto.response.reports;

public class PopularDTO {
    private Long id;
    private String name;
    private String manufacturer;
    private Integer salesCount;

    // Конструкторы
    public PopularDTO() {
    }

    public PopularDTO(Long id, String name,
                               String manufacturer, Integer salesCount) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.salesCount = salesCount;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(Integer salesCount) {
        this.salesCount = salesCount;
    }
}