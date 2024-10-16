public class Product {
    private String name;
    private double price;
    private String description;
    private String link;

    public Product(String name, double price, String description, String link) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
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
        return "\nProduct{" +
                "\nname='" + name + '\'' +
                "\nprice=" + price +
                "\ndescription='" + description + '\'' +
                "\nlink='" + link + '\'' +
                "\n}";
    }
}
