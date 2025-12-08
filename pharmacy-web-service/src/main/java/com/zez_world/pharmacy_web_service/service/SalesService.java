package com.zez_world.pharmacy_web_service.service;

import com.zez_world.pharmacy_web_service.entity.SaleReport;
import com.zez_world.pharmacy_web_service.repository.SaleReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class SalesService {

    @Autowired
    private SaleReportRepository saleReportRepository;

    @Autowired
    private ProductService productService;

    public SaleReport recordSale(Long productId, Integer quantity, Double price) {
        Double totalAmount = quantity * price;

        SaleReport saleReport = new SaleReport();
        saleReport.setProduct(productService.getProductEntityById(productId));
        saleReport.setQuantitySold(quantity);
        saleReport.setTotalAmount(totalAmount);
        saleReport.setSaleDate(LocalDate.now());

        return saleReportRepository.save(saleReport);
    }
}