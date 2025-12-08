package com.zez_world.pharmacy_web_service.service;

import com.zez_world.pharmacy_web_service.dto.response.reports.StatisticsDTO;
import com.zez_world.pharmacy_web_service.dto.response.reports.PopularDTO;
import com.zez_world.pharmacy_web_service.entity.Product;
import com.zez_world.pharmacy_web_service.repository.SaleReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private SaleReportRepository saleReportRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;


    // НОВЫЙ МЕТОД 2: Получение популярных продуктов (аналог getPopularProducts)
    public List<PopularDTO> getPopularProducts() {
        // Получаем данные за последние 30 дней
        List<Object[]> popularProducts = saleReportRepository.findPopularProductsByPeriod(
                LocalDate.now().minusDays(30), LocalDate.now());

        // Преобразуем в PopularDTO
        List<PopularDTO> result = new ArrayList<>();

        for (Object[] row : popularProducts) {
            Product product = (Product) row[0];
            Long totalSold = ((Number) row[1]).longValue();

            PopularDTO dto = new PopularDTO();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setManufacturer(product.getManufacturer());
            dto.setSalesCount(totalSold.intValue()); // Преобразуем Long в Integer

            result.add(dto);
        }

        return result;
    }

    // НОВЫЙ МЕТОД 3: Получение статистики системы
    public StatisticsDTO getSystemStatistics() {
        StatisticsDTO stats = new StatisticsDTO();

        // Количество пользователей
        stats.setTotalUsers(userService.getAllUsers().size());

        // Количество продуктов
        stats.setTotalProducts(productService.getPublicCatalog().size());

        stats.setActiveReservations(reservationService.countActiveReservations());

        // Общая выручка
        // Получаем выручку за все время
        LocalDate startDate = LocalDate.of(2000, 1, 1); // Далекая дата в прошлом
        LocalDate endDate = LocalDate.now();
        Double totalRevenue = saleReportRepository.getTotalRevenueByPeriod(startDate, endDate);

        if (totalRevenue == null) {
            totalRevenue = 0.0;
        }

        stats.setTotalRevenue(totalRevenue.intValue()); // Преобразуем Double в Integer

        return stats;
    }
}