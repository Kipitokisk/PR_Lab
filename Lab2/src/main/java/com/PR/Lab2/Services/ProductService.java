package com.PR.Lab2.Services;

import com.PR.Lab2.Entities.Product;
import com.PR.Lab2.Repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
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
}
