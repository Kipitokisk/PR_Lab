package com.PR.Lab2.Controllers;

import com.PR.Lab2.Entities.Product;
import com.PR.Lab2.Services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.OK)
    public void saveProduct(@RequestBody List<Product> products) {
        this.productService.saveProduct(products);
    }

    @GetMapping("/{product-id}")
    public Product getProductById(@PathVariable("product-id") Integer id) {
        return this.productService.getProductById(id);
    }

    @PutMapping("/{product-id}/{product-name}")
    public Product changeProductName(@PathVariable("product-id") Integer id,
                                     @PathVariable("product-name") String name) {
        return this.productService.changeProductName(id, name);
    }

    @DeleteMapping("/{product-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(@PathVariable("product-id") Integer id) {
        this.productService.deleteProductById(id);
    }

    @GetMapping
    public Page<Product> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.getProductsByPage(pageable);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadProducts(@RequestParam("file") MultipartFile file) {
        try {
            productService.saveProductsFromJson(file);
            return ResponseEntity.status(HttpStatus.OK).body("Products uploaded successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload products: " + e.getMessage());
        }
    }
}
