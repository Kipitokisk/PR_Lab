import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebScraper {
    public void scrapeWebsite(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements products = document.select("ul.ads-list-photo.large-photo > li.ads-list-photo-item");
//            System.out.println(products);
            for (Element product : products) {
                String name = product.select(".ads-list-photo-item-title").text();
                String priceString = product.select(".ads-list-photo-item-price-wrapper").text();
                String productUrl = "https://999.md" + product.select(".js-item-ad").attr("href");

                ProductValidator productValidator = new ProductValidator();
                name = productValidator.cleanString(name);
                Integer price = productValidator.validatePrice(priceString);

                if (price == null) {
                    continue;
                }

                Document productPage = Jsoup.connect(productUrl).get();
                String description = productPage.select(".adPage__content__description.grid_18").text();

                Product productBody = new Product(name, price, description, productUrl);

//                System.out.println(productBody);

                System.out.println("productName:" + name);
                System.out.println("productPrice: " + price);
                System.out.println("productDescription: " + description);
                System.out.println(productUrl);
                System.out.println();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
