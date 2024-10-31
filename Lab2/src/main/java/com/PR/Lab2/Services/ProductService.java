package com.PR.Lab2.Services;

import com.PR.Lab2.Entities.Product;
import com.PR.Lab2.Repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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


}
