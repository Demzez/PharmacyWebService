package com.zez_world.pharmacy_web_service.dto.response.reports;

public class StatisticsDTO {
    private Integer totalUsers;
    private Integer totalProducts;
    private Integer activeReservations;
    private Integer totalRevenue;

    public StatisticsDTO() {
    }

    public StatisticsDTO(Integer totalUsers, Integer totalProducts,
                              Integer activeReservations, Integer totalRevenue) {
        this.totalUsers = totalUsers;
        this.totalProducts = totalProducts;
        this.activeReservations = activeReservations;
        this.totalRevenue = totalRevenue;
    }

    // Геттеры и сеттеры
    public Integer getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Integer totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Integer getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(Integer totalProducts) {
        this.totalProducts = totalProducts;
    }

    public Integer getActiveReservations() {
        return activeReservations;
    }

    public void setActiveReservations(Integer activeReservations) {
        this.activeReservations = activeReservations;
    }

    public Integer getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Integer totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}