public class Product {
    private String name;
    private double price;
    private String description;
    private String url;

    public Product(String name, double price, String description, String url) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    // Custom serialization to JSON
    public String toJson() {
        return String.format("{\n  \"name\": \"%s\",\n  \"price\": %.2f,\n  \"description\": \"%s\",\n  \"url\": \"%s\"\n}",
                name, price, description, url);
    }

    // Custom serialization to XML
    public String toXml() {
        return String.format("<Product>\n  <Name>%s</Name>\n  <Price>%.2f</Price>\n  <Description>%s</Description>\n  <Url>%s</Url>\n</Product>",
                name, price, description, url);
    }

    @Override
    public String toString() {
        return "\nProduct{" +
                "\nname='" + name + '\'' +
                "\nprice=" + price +
                "\ndescription='" + description + '\'' +
                "\nurl='" + url + '\'' +
                "\n}";
    }
}
