public class Product {
    private String name;
    private Integer price;
    private String description;
    private String link;

    public Product(String name, Integer price, String description, String link) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
