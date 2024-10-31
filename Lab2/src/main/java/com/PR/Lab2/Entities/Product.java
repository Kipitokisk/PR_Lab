package com.PR.Lab2.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "t_products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private long price;
    @Column(columnDefinition = "VARCHAR(5000)")
    private String description;
    @Column(columnDefinition = "VARCHAR(500)")
    private String url;

    public Product() {
    }

    public Product(String name, long price, String description, String url) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
