package com.zez_world.pharmacy_web_service.service;

import com.zez_world.pharmacy_web_service.entity.SaleReport;
import com.zez_world.pharmacy_web_service.repository.SaleReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private SaleReportRepository saleReportRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    public Map<String, Object> getSalesReport(LocalDate startDate, LocalDate endDate) {
        List<SaleReport> reports = saleReportRepository.findBySaleDateBetween(startDate, endDate);
        Double totalRevenue = saleReportRepository.getTotalRevenueByPeriod(startDate, endDate);
        List<SaleReport> topSelling = saleReportRepository.findTopSellingProductsByPeriod(startDate, endDate);

        Map<String, Object> result = new HashMap<>();
        result.put("period", startDate + " - " + endDate);
        result.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
        result.put("totalSales", reports.size());
        result.put("detailedReports", reports);
        result.put("topSellingProducts", topSelling);

        return result;
    }

    public Map<String, Object> getPopularProducts() {
        List<Object[]> popularProducts = saleReportRepository.findPopularProductsByPeriod(
                LocalDate.now().minusDays(30), LocalDate.now());

        Map<String, Object> result = new HashMap<>();
        result.put("analysisPeriod", "Last 30 days");
        result.put("popularProducts", popularProducts);

        return result;
    }

    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProducts", productService.getPublicCatalog().size());
        stats.put("availableProducts", productService.getAvailableProducts().size());
        stats.put("totalUsers", userService.getAllUsers().size());

        return stats;
    }

    public SaleReport recordSale(Long productId, Integer quantity, Double price) {
        // Метод для записи продажи (вызывается при завершении бронирования)
        Double totalAmount = quantity * price;

        SaleReport saleReport = new SaleReport();
        saleReport.setProduct(productService.getProductEntityById(productId));
        saleReport.setQuantitySold(quantity);
        saleReport.setTotalAmount(totalAmount);
        saleReport.setSaleDate(LocalDate.now());

        return saleReportRepository.save(saleReport);
    }
}