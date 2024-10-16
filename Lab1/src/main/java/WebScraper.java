import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class WebScraper {
    public void scrapeWebsite(String url, double minPrice, double maxPrice) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements products = document.select("ul.ads-list-photo.large-photo > li.ads-list-photo-item");
            List<Product> filteredProducts = products.stream()
                    .map(product -> {
                        String name = product.select(".ads-list-photo-item-title").text();
                        String priceString = product.select(".ads-list-photo-item-price-wrapper").text();
                        String productUrl = "https://999.md" + product.select(".js-item-ad").attr("href");

                        ProductValidator productValidator = new ProductValidator();
                        name = productValidator.cleanString(name);
                        Integer price = productValidator.validatePrice(priceString);

                        if (price == null) {
                            return null;
                        }
                        double priceInMdl = productValidator.convertPrice(price, priceString);

                        try {
                            Document productPage = Jsoup.connect(productUrl).get();
                            String description = productPage.select(".adPage__content__description.grid_18").text();
                            return new Product(name, priceInMdl, description, productUrl);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }).filter(product -> product != null && product.getPrice() >= minPrice && product.getPrice() <= maxPrice)
                    .collect(Collectors.toList());

            double totalPrice = filteredProducts.stream().map(Product::getPrice).reduce(0.0, Double::sum);

            ProductSummary productSummary = new ProductSummary(filteredProducts, totalPrice, Instant.now().toString());

            System.out.println(productSummary);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
