import java.text.DecimalFormat;
import java.util.List;

public class ProductSummary {
    private List<Product> products;
    private double totalPrice;
    private String utcTimestamp;

    public ProductSummary(List<Product> products, double totalPrice, String utcTimestamp) {
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

    // Custom serialization to JSON
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

    // Custom serialization to XML
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
        DecimalFormat df = new DecimalFormat("#,###.00");
        return "ProductSummary{" +
                "\nproducts=" + products +
                "\ntotalPrice=" + df.format(totalPrice) +
                "\nutcTimestamp='" + utcTimestamp + '\'' +
                '}';
    }
}
