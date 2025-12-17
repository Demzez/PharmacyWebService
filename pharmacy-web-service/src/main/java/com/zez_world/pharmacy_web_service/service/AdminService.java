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


    public List<PopularDTO> getPopularProducts() {
        List<Object[]> popularProducts = saleReportRepository.findPopularProductsByPeriod(
                LocalDate.now().minusDays(30), LocalDate.now());

        List<PopularDTO> result = new ArrayList<>();

        for (Object[] row : popularProducts) {
            Product product = (Product) row[0];
            Long totalSold = ((Number) row[1]).longValue();

            PopularDTO dto = new PopularDTO();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setManufacturer(product.getManufacturer());
            dto.setSalesCount(totalSold.intValue());

            result.add(dto);
        }

        return result;
    }

    public StatisticsDTO getSystemStatistics() {
        StatisticsDTO stats = new StatisticsDTO();

        stats.setTotalUsers(userService.getAllUsers().size());

        stats.setTotalProducts(productService.getPublicCatalog().size());

        stats.setActiveReservations(reservationService.countActiveReservations());

        LocalDate startDate = LocalDate.of(2000, 1, 1);
        LocalDate endDate = LocalDate.now();
        Double totalRevenue = saleReportRepository.getTotalRevenueByPeriod(startDate, endDate);

        if (totalRevenue == null) {
            totalRevenue = 0.0;
        }

        stats.setTotalRevenue(totalRevenue.intValue());

        return stats;
    }
}