import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WebScraper {
    public void scrapeWebsite(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements products = document.select(".ads-list-photo.large-photo");
            //System.out.println(products);
            for (Element product : products) {
                String name = product.select(".ads-list-photo-item-title").text();
                String priceString = product.select(".ads-list-photo-item-price-wrapper").text();
                System.out.println("Product: productName:" + name + " " + "prodcutPrice: " + priceString);
                System.out.println();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
