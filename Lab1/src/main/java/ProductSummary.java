import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductSummary {
    private List<Product> products;
    private long totalPrice;
    private String utcTimestamp;
    DecimalFormat df = new DecimalFormat("#,###.00");

    public ProductSummary(List<Product> products, long totalPrice, String utcTimestamp) {
        this.products = products;
        this.totalPrice = totalPrice;
        this.utcTimestamp = utcTimestamp;
    }

    public List<Product> getProducts() {
        return products;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getUtcTimestamp() {
        return utcTimestamp;
    }

    public String toPipeSeparated() {
        StringBuilder builder = new StringBuilder();
        builder.append("ProductSummary|\n")
                .append("  totalPrice|").append(df.format(totalPrice)).append("\n")
                .append("  utcTimestamp|").append(utcTimestamp).append("\n")
                .append("  products|[\n");

        // Serialize each product on a new line with indentation
        for (Product product : products) {
            builder.append("    ").append(product.toPipeSeparated().replaceAll("\n", "\n    ")).append("\n");
        }
        builder.append("  ]");

        return builder.toString();
    }

    public static ProductSummary fromPipeSeparated(String serializedSummary) {
        String[] lines = serializedSummary.split("\\n");
        if (!lines[0].startsWith("ProductSummary")) {
            throw new IllegalArgumentException("Invalid pipe-separated format for ProductSummary");
        }

        long totalPrice = Long.parseLong(lines[1].split("\\|")[1].trim());
        String utcTimestamp = lines[2].split("\\|")[1].trim();
        List<Product> products = new ArrayList<>();

        // Collect all products from the lines
        int productStartIndex = 4; // Products start after line 3
        for (int i = productStartIndex; i < lines.length - 1; i += 5) {
            StringBuilder productBuilder = new StringBuilder();
            for (int j = 0; j < 5; j++) {  // Products span 5 lines
                productBuilder.append(lines[i + j].replaceAll("    ", "")).append("\n");
            }
            products.add(Product.fromPipeSeparated(productBuilder.toString().trim()));
        }

        return new ProductSummary(products, totalPrice, utcTimestamp);
    }


    public String toJson() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n  \"products\": [\n");
        for (int i = 0; i < products.size(); i++) {
            jsonBuilder.append(products.get(i).toJson());
            if (i < products.size() - 1) {
                jsonBuilder.append(",\n");
            }
        }
        jsonBuilder.append("\n  ],\n  \"totalPrice\": ").append(totalPrice).append(",\n  \"timestamp\": \"").append(utcTimestamp).append("\"\n}");
        return jsonBuilder.toString();
    }

    public String toXml() {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<ProductSummary>\n  <TotalPrice>").append(totalPrice).append("</TotalPrice>\n  <Timestamp>")
                .append(utcTimestamp).append("</Timestamp>\n  <Products>\n");
        for (Product product : products) {
            xmlBuilder.append(product.toXml()).append("\n");
        }
        xmlBuilder.append("  </Products>\n</ProductSummary>");
        return xmlBuilder.toString();
    }

    @Override
    public String toString() {
        return "ProductSummary{" +
                "\nproducts=" + products +
                "\ntotalPrice=" + totalPrice +
                "\nutcTimestamp='" + utcTimestamp + '\'' +
                '}';
    }
}
