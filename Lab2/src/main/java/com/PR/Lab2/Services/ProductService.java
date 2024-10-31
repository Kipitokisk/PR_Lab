package com.PR.Lab2.Services;

import com.PR.Lab2.Entities.Product;
import com.PR.Lab2.Repositories.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public ProductService(ProductRepository productRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void saveProduct(List<Product> products) {
        productRepository.saveAll(products);
    }

    @Transactional
    public Product getProductById(Integer id) {
        return productRepository.findProductById(id);
    }

    @Transactional
    public Product changeProductName(Integer id, String name) {
        Product product = productRepository.findProductById(id);
        product.setName(name);
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProductById(Integer id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public Page<Product> getProductsByPage(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Transactional
    public void saveProductsFromJson(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<Product> products = objectMapper.readValue(reader, new TypeReference<List<Product>>() {});

            for (Product product : products) {
                if (product.getName() != null && product.getName().length() > 255) {
                    throw new IllegalArgumentException("Product name exceeds maximum length of 255 characters");
                }
                if (product.getUrl() != null && product.getUrl().length() > 500) {
                    throw new IllegalArgumentException("Product URL exceeds maximum length of 500 characters");
                }
                if (product.getDescription() != null && product.getDescription().length() > 5000) {
                    throw new IllegalArgumentException("Product description exceeds maximum length of 5000 characters");
                }
            }

            productRepository.saveAll(products);
        }
    }
}
