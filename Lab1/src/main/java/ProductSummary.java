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

    @Override
    public String toString() {
        return "ProductSummary{" +
                "products=" + products +
                ", totalPrice=" + totalPrice +
                ", utcTimestamp='" + utcTimestamp + '\'' +
                '}';
    }
}
