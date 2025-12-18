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

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProducts(@RequestParam String query) {
        return ResponseEntity.ok(productService.searchProducts(query));
    }


    @GetMapping("/{id}/analogs")
    public ResponseEntity<List<AnalogProductDTO>> getAnalogs(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findAnalogs(id));
    }

}