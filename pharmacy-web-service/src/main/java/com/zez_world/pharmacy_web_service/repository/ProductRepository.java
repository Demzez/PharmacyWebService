package com.zez_world.pharmacy_web_service.repository;

import com.zez_world.pharmacy_web_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByVisibleTrue();
    List<Product> findByVisibleTrueAndStockQuantityGreaterThan(Integer quantity);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByActiveSubstanceContainingIgnoreCase(String activeSubstance);
    List<Product> findByCategoryContainingIgnoreCase(String category);

    @Query("SELECT p FROM Product p WHERE p.activeSubstance = :activeSubstance AND p.category = :category  AND p.id != :excludeId AND p.visible = true")
    List<Product> findAnalogsByActiveSubstanceAndCategory(@Param("activeSubstance") String activeSubstance,
                                                          @Param("category") String category,
                                                          @Param("excludeId") Long excludeId);

    @Query("SELECT p FROM Product p WHERE p.visible = true AND (" +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.activeSubstance) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.category) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.manufacturer) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Product> universalSearch(@Param("query") String query);

    @Query("SELECT p FROM Product p WHERE (" +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.activeSubstance) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.category) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.manufacturer) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Product> universalAdminSearch(@Param("query") String query);

    @Query("SELECT p FROM Product p WHERE p.visible = true AND p.stockQuantity > 0 ORDER BY p.stockQuantity DESC LIMIT 10")
    List<Product> findPopularProducts();
}