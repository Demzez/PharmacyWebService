package com.zez_world.pharmacy_web_service.service;

import com.zez_world.pharmacy_web_service.dto.request.ProductCreateDTO;
import com.zez_world.pharmacy_web_service.dto.response.AnalogProductDTO;
import com.zez_world.pharmacy_web_service.dto.response.ProductResponseDTO;
import com.zez_world.pharmacy_web_service.entity.Product;
import com.zez_world.pharmacy_web_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductResponseDTO> getPublicCatalog() {
        return productRepository.findByVisibleTrue()
                .stream()
                .map(ProductResponseDTO::fromPublic)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getAvailableProducts() {
        return productRepository.findByVisibleTrueAndStockQuantityGreaterThan(0)
                .stream()
                .map(ProductResponseDTO::fromPublic)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> searchProducts(String query) {
        return productRepository.universalSearch(query)
                .stream()
                .map(ProductResponseDTO::fromPublic)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(ProductResponseDTO::fromPublic)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> searchByActiveSubstance(String activeSubstance) {
        return productRepository.findByActiveSubstanceContainingIgnoreCase(activeSubstance)
                .stream()
                .map(ProductResponseDTO::fromPublic)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> searchByCategory(String category) {
        return productRepository.findByCategoryContainingIgnoreCase(category)
                .stream()
                .map(ProductResponseDTO::fromPublic)
                .collect(Collectors.toList());
    }

    public List<AnalogProductDTO> findAnalogs(Long productId) {
        Product original = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductResponseDTO originalDto = ProductResponseDTO.fromPublic(original);

        return productRepository.findAnalogsByActiveSubstanceAndCategory(original.getActiveSubstance(), original.getCategory(), productId)
                .stream()
                .map(ProductResponseDTO::fromPublic)
                .map(productDto -> AnalogProductDTO.from(productDto, originalDto))
                .collect(Collectors.toList());
    }

    public ProductResponseDTO createProduct(ProductCreateDTO productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setManufacturer(productDto.getManufacturer());
        product.setReleaseForm(productDto.getReleaseForm());
        product.setExpiryDate(productDto.getExpiryDate());
        product.setPrescriptionStatus(productDto.getPrescriptionStatus());
        product.setPrice(productDto.getPrice());
        product.setStockQuantity(productDto.getStockQuantity());
        product.setActiveSubstance(productDto.getActiveSubstance());
        product.setCategory(productDto.getCategory());
        product.setVisible(true);

        Product savedProduct = productRepository.save(product);
        return ProductResponseDTO.fromAdmin(savedProduct);
    }

    public ProductResponseDTO updateProduct(Long id, ProductCreateDTO productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productDto.getName());
        product.setManufacturer(productDto.getManufacturer());
        product.setReleaseForm(productDto.getReleaseForm());
        product.setExpiryDate(productDto.getExpiryDate());
        product.setPrescriptionStatus(productDto.getPrescriptionStatus());
        product.setPrice(productDto.getPrice());
        product.setStockQuantity(productDto.getStockQuantity());
        product.setActiveSubstance(productDto.getActiveSubstance());
        product.setCategory(productDto.getCategory());

        Product updatedProduct = productRepository.save(product);
        return ProductResponseDTO.fromAdmin(updatedProduct);
    }

    public ProductResponseDTO toggleProductVisibility(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setVisible(!product.isVisible());
        Product updatedProduct = productRepository.save(product);
        return ProductResponseDTO.fromAdmin(updatedProduct);
    }

    public void updateStockQuantity(Long productId, Integer quantityChange) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int newQuantity = product.getStockQuantity() + quantityChange;
        if (newQuantity < 0) {
            throw new RuntimeException("Insufficient stock");
        }

        product.setStockQuantity(newQuantity);
        productRepository.save(product);
    }

    public Optional<ProductResponseDTO> getProductById(Long id) {
        return productRepository.findById(id)
                .map(ProductResponseDTO::fromPublic);
    }

    public List<ProductResponseDTO> getPopularProducts() {
        return productRepository.findPopularProducts()
                .stream()
                .map(ProductResponseDTO::fromPublic)
                .collect(Collectors.toList());
    }

    public Product getProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
}