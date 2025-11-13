package com.zez_world.pharmacy_web_service.controller;

import com.zez_world.pharmacy_web_service.dto.response.AnalogProductDTO;
import com.zez_world.pharmacy_web_service.dto.response.ProductResponseDTO;
import com.zez_world.pharmacy_web_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/catalog")
    public ResponseEntity<List<ProductResponseDTO>> getPublicCatalog() {
        return ResponseEntity.ok(productService.getPublicCatalog());
    }

    @GetMapping("/available")
    public ResponseEntity<List<ProductResponseDTO>> getAvailableProducts() {
        return ResponseEntity.ok(productService.getAvailableProducts());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProducts(@RequestParam String query) {
        return ResponseEntity.ok(productService.searchProducts(query));
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<ProductResponseDTO>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(productService.searchByName(name));
    }

    @GetMapping("/search/substance")
    public ResponseEntity<List<ProductResponseDTO>> searchByActiveSubstance(@RequestParam String substance) {
        return ResponseEntity.ok(productService.searchByActiveSubstance(substance));
    }

    @GetMapping("/search/category")
    public ResponseEntity<List<ProductResponseDTO>> searchByCategory(@RequestParam String category) {
        return ResponseEntity.ok(productService.searchByCategory(category));
    }

    @GetMapping("/{id}/analogs")
    public ResponseEntity<List<AnalogProductDTO>> getAnalogs(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findAnalogs(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<Map<String, Object>> checkAvailability(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("available", product.getStockQuantity() > 0);
                    response.put("quantity", product.getStockQuantity());
                    response.put("product", product);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ProductResponseDTO>> getPopularProducts() {
        return ResponseEntity.ok(productService.getPopularProducts());
    }
}