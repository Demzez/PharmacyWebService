package com.zez_world.pharmacy_web_service.repository;

import com.zez_world.pharmacy_web_service.entity.SaleReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleReportRepository extends JpaRepository<SaleReport, Long> {
    List<SaleReport> findBySaleDateBetween(LocalDate startDate, LocalDate endDate);
    List<SaleReport> findByProductId(Long productId);

    @Query("SELECT sr FROM SaleReport sr WHERE sr.saleDate BETWEEN :startDate AND :endDate ORDER BY sr.totalAmount DESC")
    List<SaleReport> findTopSellingProductsByPeriod(@Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(sr.totalAmount) FROM SaleReport sr WHERE sr.saleDate BETWEEN :startDate AND :endDate")
    Double getTotalRevenueByPeriod(@Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate);

    @Query("SELECT sr.product, SUM(sr.quantitySold) as totalSold FROM SaleReport sr " +
            "WHERE sr.saleDate BETWEEN :startDate AND :endDate " +
            "GROUP BY sr.product ORDER BY totalSold DESC")
    List<Object[]> findPopularProductsByPeriod(@Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);
}