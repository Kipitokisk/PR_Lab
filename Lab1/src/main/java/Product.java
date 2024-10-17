public class Product {
    private String name;
    private long price;
    private String description;
    private String url;

    public Product(String name, long price, String description, String url) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String toPipeSeparated() {
        return "Product|\n" +
                "  name|" + name + "\n" +
                "  price|" + price + "\n" +
                "  description|" + description + "\n" +
                "  url|" + url;
    }

    public static Product fromPipeSeparated(String serializedProduct) {
        String[] lines = serializedProduct.split("\\n");
        if (!lines[0].startsWith("Product")) {
            throw new IllegalArgumentException("Invalid pipe-separated format for Product");
        }

        String name = lines[1].split("\\|")[1].trim();
        long price = Long.parseLong(lines[2].split("\\|")[1].trim());
        String description = lines[3].split("\\|")[1].trim();
        String url = lines[4].split("\\|")[1].trim();

        return new Product(name, price, description, url);
    }


    public String toJson() {
        return String.format("{\n  \"name\": \"%s\",\n  \"price\": %d,\n  \"description\": \"%s\",\n  \"url\": \"%s\"\n}",
                name, price, description, url);
    }

    public String toXml() {
        return String.format("<Product>\n  <Name>%s</Name>\n  <Price>%d</Price>\n  <Description>%s</Description>\n  <Url>%s</Url>\n</Product>",
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
