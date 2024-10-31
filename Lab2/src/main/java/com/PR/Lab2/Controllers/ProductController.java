package com.PR.Lab2.Controllers;

import com.PR.Lab2.Entities.Product;
import com.PR.Lab2.Services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/save")
    public Product saveProduct(@RequestBody Product product) {
        return this.productService.saveProduct(product);
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
}
